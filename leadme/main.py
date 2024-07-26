from fastapi import FastAPI, File, UploadFile, Form
from pydantic import BaseModel
import shutil
import os
from video_processor import download_video, process_video, process_video_user

app = FastAPI()

class Video(BaseModel):
    url : str
    youtubeId : str

UPLOAD_DIRECTORY = "."

@app.get("/")
async def read_root():
    return "This is root path from MyAPI"

@app.post("/upload/videoUrl")
async def saveVideoDataByUrl(video: Video):
    video_path = download_video(video.url, 'downloaded_video.mp4')
    keypoints = process_video(video.youtubeId, video_path)
    return {"youtubeId": video.youtubeId, "keypoints": keypoints}

# 유저 영상을 다운로드 받는데 데이터를 저장안하고 넘어갈껄?, 
@app.post("/upload/userFile")
async def saveVideDataByUserFile(videoFile : UploadFile = File(...), filename: str = Form(...)):
    video_path = os.path.join(UPLOAD_DIRECTORY, filename+".mp4")
    
    with open(video_path, "wb") as buffer:
        shutil.copyfileobj(videoFile.file, buffer)

    print("execute")

    keypoints = process_video_user(video_path)
    return {"keypoints": keypoints}