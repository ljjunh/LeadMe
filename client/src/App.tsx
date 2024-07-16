import React from "react";
// import Button from "./components/TestBtn";
import { RecoilRoot } from "recoil";
import { MainCanvas } from "./components/MainCanvas";
import styled from "styled-components";
const App: React.FC = () => {
  return (
    <RecoilRoot>
      <Wrapper>
        {/* <div>
        <Button label="Send Request" url="YOUR_BACKEND_URL_HERE" />
        </div> */}
        <MainCanvas />
      </Wrapper>
    </RecoilRoot>
  );
};

export default App;

const Wrapper = styled.div`
  width: 100vw;
  height: 100vh;
  overflow: hidden;
`;
