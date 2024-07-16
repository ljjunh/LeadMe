import { useAnimations, useGLTF, useScroll } from "@react-three/drei";
import { useEffect, useRef } from "react";
import { useRecoilValue } from "recoil";
import { IsEnteredAtom } from "../stores";
import { Loader } from "./Loader";

export const Dancer = () => {
  const isEntered = useRecoilValue(IsEnteredAtom);
  const { scene, animations } = useGLTF("/models/dancer.glb");
  const dancerRef = useRef(null);

  const { actions } = useAnimations(animations, dancerRef);

  const scroll = useScroll();

  useEffect(() => {
    if (!isEntered) return;
    actions["wave"]?.play();
  }, [actions, isEntered]);

  if (isEntered) {
    return (
      <>
        <ambientLight intensity={2} />
        <primitive ref={dancerRef} object={scene} scale={0.05}></primitive>;
      </>
    );
  }
  return <Loader isCompleted />;
};
