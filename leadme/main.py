from fastapi import FastAPI, File, UploadFile
from pydantic import BaseModel
import shutil
import os
import uuid
import config
import ray
import cv2
import time
import logging
from pathlib import Path
import asyncio
from moviepy.editor import VideoFileClip
from video_processor import download_video, process_video, process_video_user

# 로깅 설정
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(message)s')
logger = logging.getLogger(__name__)

print(Path(__file__).resolve())

app = FastAPI()

# Ray 초기화
ray.init()

class Video(BaseModel):
    url : str
    youtubeId : str

UPLOAD_DIRECTORY = "."
TEMP_DIRECTORY = "/home/ubuntu/python/video/temporary"
PERMANENT_DIRECTORY_USER = "/home/ubuntu/python/video/user"
PERMANENT_DIRECTORY_CHALLENGE = "/home/ubuntu/python/video/challenge"

## 서비스 로직 호출 부분을 Ray로 병렬 처리한다.
@ray.remote
def ray_process_video(youtubeId, video_path):
    return process_video(youtubeId, video_path)

@ray.remote
def ray_process_video_user(video_path):
    return process_video_user(video_path)

@app.get("/")
async def read_root():
    return "This is root path from MyAPI"

@app.post("/videoUrl")
async def saveVideoData(video: Video):
    start_time = time.time()
    video_path = await asyncio.to_thread(download_video, video.url, 'downloaded_video.mp4')
    
    # 비디오 처리 실행 및 결과 대기
    keypoints = await asyncio.to_thread(lambda: ray.get(ray_process_video.remote(video.youtubeId, video_path)))
    
    total_time = time.time() - start_time
    logger.info(f"videoUrl API - YoutubeID: {video.youtubeId}, Total Time: {total_time:.4f} seconds")
    
    return {"youtubeId": video.youtubeId, "keypoints": keypoints}

@app.post("/upload/userFile")
async def saveVideDataByUserFile(videoFile: UploadFile = File(...)):
    start_time = time.time()
    unique_id = str(uuid.uuid4())

    
    os.makedirs(TEMP_DIRECTORY, exist_ok=True)
    original_video_path = os.path.join(TEMP_DIRECTORY, f"{unique_id}_{videoFile.filename}")
    flipped_temp_video_path = os.path.join(TEMP_DIRECTORY, f"{unique_id}_flipped_temp.avi")
    final_video_path = os.path.join(TEMP_DIRECTORY, f"{unique_id}.mp4")

    # 파일을 TEMP_DIRECTORY에 원래 이름으로 저장
    download_start = time.time()
    with open(original_video_path, "wb") as buffer:
        shutil.copyfileobj(videoFile.file, buffer)
    download_end = time.time()

    # 비디오 파일을 수평으로 뒤집고 임시 파일로 저장
    try:
        flip_start = time.time()
        # 원본 비디오를 읽어와서 뒤집기
        cap = cv2.VideoCapture(original_video_path)
        fourcc = cv2.VideoWriter_fourcc(*'XVID')
        out = cv2.VideoWriter(flipped_temp_video_path, fourcc, cap.get(cv2.CAP_PROP_FPS), 
                            (int(cap.get(cv2.CAP_PROP_FRAME_WIDTH)), int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))))

        while cap.isOpened():
            ret, frame = cap.read()
            if not ret:
                break
            flipped_frame = cv2.flip(frame, 1)  # 수평으로 뒤집기
            out.write(flipped_frame)

        cap.release()
        out.release()
        flip_end = time.time()

        convert_start = time.time()
        # 뒤집힌 비디오 파일을 mp4로 변환
        clip = VideoFileClip(flipped_temp_video_path)
        clip.write_videofile(final_video_path, codec="libx264")
        convert_end = time.time()
    except Exception as e:
        return {"error": str(e)}
    
    # 원본 비디오 파일과 임시 파일 삭제
    os.remove(original_video_path)
    os.remove(flipped_temp_video_path)

    # 비디오 처리 실행 및 결과 대기
    keypoints = await asyncio.to_thread(lambda: ray.get(ray_process_video_user.remote(final_video_path)))

    total_time = time.time() - start_time
    logger.info(f"userFile API - UUID: {unique_id}, Total Time: {total_time:.4f} seconds")

    return {"uuid": unique_id, "keypoints": keypoints}
