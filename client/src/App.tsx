import React from "react";
import { Routes, Route } from "react-router-dom";
import { RecoilRoot } from "recoil";
import { MainCanvas } from "./components/MainCanvas";
import styled from "styled-components";

const App: React.FC = () => {
  return (
    <RecoilRoot>
      <Routes>
        <Route
          path="/"
          element={
            <Wrapper>
              <MainCanvas />
            </Wrapper>
          }
        ></Route>
      </Routes>
    </RecoilRoot>
  );
};

export default App;

const Wrapper = styled.div`
  width: 100vw;
  height: 100vh;
  overflow: hidden;
`;
