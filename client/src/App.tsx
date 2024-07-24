import React from "react";
import theme from "./theme";
import { Routes, Route } from "react-router-dom";
import { MainCanvas } from "./components/MainCanvas";
import { FixedDOM } from "./components/dom/FixedDOM";
import { ThemeProvider } from "styled-components";
import { VideoDetail } from "./pages/VideoDetail";
import GlobalStyle from "./globalStyles";
import styled from "styled-components";
import Home from "./pages/Home";
import Rank from "./pages/Rank";
import Search from "./pages/Search";
import Report from "./pages/Report";
import Challenge from "./pages/Challenge";
import Chat from "./pages/Chat";
import Mypage from "./pages/Mypage";

const App: React.FC = () => {
  return (
    <ThemeProvider theme={theme}>
      <GlobalStyle />
      <Routes>
        <Route
          path="/"
          element={
            <Wrapper>
              <MainCanvas />
              <FixedDOM />
            </Wrapper>
          }
        ></Route>
        <Route path="/home" element={<Home />}></Route>
        <Route path="/rank" element={<Rank />}></Route>
        <Route path="/search" element={<Search />}></Route>
        <Route path="/search/:query" element={<VideoDetail />}></Route>
        <Route path="/challenge" element={<Challenge />}></Route>
        <Route path="/mypage" element={<Mypage />}></Route>
        <Route path="/report" element={<Report />}></Route>
        <Route path="/chat" element={<Chat />}></Route>
      </Routes>
    </ThemeProvider>
  );
};

export default App;

const Wrapper = styled.div`
  width: 100vw;
  height: 100vh;
  overflow: hidden;
`;
