import React from "react";
import { Route, Routes } from "react-router-dom";
import jwt_decode from "jwt-decode";
import { ErrorBoundary } from "react-error-boundary";
import AppNavbar from "./AppNavbar";
import Home from "./home";
import PrivateRoute from "./privateRoute";
import Register from "./auth/register";
import Login from "./auth/login";
import Logout from "./auth/logout";
import PlanList from "./public/plan";
import tokenService from "./services/token.service";
import UserListAdmin from "./admin/users/UserListAdmin";
import UserEditAdmin from "./admin/users/UserEditAdmin";
import SwaggerDocs from "./public/swagger";
import CreateGame from "./game/create";
import GameWaitingRoom from "./game/waitingRoom";
import JoinGame from "./game/join";
import Profile from "./profile";
import EditProfile from "./profile/EditProfile";
import AchievementList from "./achievements/achievementList";
import StatisticsPage from "./statistics";
import GameList from "./game/gameList";
import AchievementEdit from "./achievements/achievementEdit";
import AdminGamesList from "./admin/matches"
import Friends from "./friends";
import { Fila } from "./components/game/fila/fila";
import { InitBoard } from "./game/board/index"
import FilaFinal from "./game/board/filaFinal"

import ViewResults from "./admin/matches/viewResults";

import FinishedMatches from "./admin/matches/finishedMatches";



function ErrorFallback({ error, resetErrorBoundary }) {
  return (
    <div role="alert" style={{ padding: 16 }}>
      <p>Something went wrong:</p>
      <pre style={{ whiteSpace: "pre-wrap" }}>{error.message}</pre>
      <button className="btn" onClick={resetErrorBoundary}>Try again</button>
    </div>
  );
}

function App() {
  const jwt = tokenService.getLocalAccessToken();
  let roles = [];
  if (jwt) roles = jwt_decode(jwt).authorities || [];

  let adminRoutes = <></>;
  let ownerRoutes = <></>;
  let userRoutes = <></>;
  let vetRoutes = <></>;
  let publicRoutes = <></>;

  roles.forEach((role) => {
    if (role === "ADMIN") {
      adminRoutes = (
        <>
          <Route path="/users"               exact={true} element={<PrivateRoute><UserListAdmin /></PrivateRoute>} />
          <Route path="/users/:username"     exact={true} element={<PrivateRoute><UserEditAdmin /></PrivateRoute>} />
          <Route path="/matches"             exact={true} element={<PrivateRoute><AdminGamesList /></PrivateRoute>} />
          <Route path="/achievements"        exact={true} element={<PrivateRoute><AchievementList /></PrivateRoute>} />
          <Route path="/achievements/:achievementId" exact={true} element={<PrivateRoute><AchievementEdit /></PrivateRoute>} />
          <Route path="/profile"                   element={<PrivateRoute><Profile /></PrivateRoute>} />
          <Route path="/profile/editProfile"       element={<PrivateRoute><EditProfile /></PrivateRoute>} />
          <Route path = "/matches/:gameId/viewResults" element = {<PrivateRoute><ViewResults /></PrivateRoute>} />
        </>
      );
    }
    if (role === "PLAYER") {
      ownerRoutes = (
        <>
          <Route path="/create-game"               element={<PrivateRoute><CreateGame /></PrivateRoute>} />
          <Route path="/games/:id"                 element={<PrivateRoute><GameWaitingRoom /></PrivateRoute>} />
          <Route path="/join-game"                 element={<PrivateRoute><JoinGame /></PrivateRoute>} />
          <Route path="/games/friends"             element={<PrivateRoute><Friends /></PrivateRoute>} />
          <Route path="/games"             element={<PrivateRoute><GameList /></PrivateRoute>} />
          <Route path="/statistics"                element={<PrivateRoute><StatisticsPage /></PrivateRoute>} />
          <Route path="/profile"                   element={<PrivateRoute><Profile /></PrivateRoute>} />
          <Route path="/profile/editProfile"       element={<PrivateRoute><EditProfile /></PrivateRoute>} />
          <Route path="/games/board"               element={<PrivateRoute><Fila /></PrivateRoute>} />
          <Route path="/games/board/prueba"        element={<PrivateRoute><InitBoard/></PrivateRoute>} />
          <Route path="/achievements"        exact={true} element={<PrivateRoute><AchievementList /></PrivateRoute>} />

          <Route path="/matches"               element={<PrivateRoute><FinishedMatches /></PrivateRoute>} />
          <Route path = "/matches/:gameId/viewResults" element = {<PrivateRoute><ViewResults /></PrivateRoute>} />
        </>
      );
    }
  });

  if (!jwt) {
    publicRoutes = (
      <>
        <Route path="/register" element={<Register />} />
        <Route path="/login"    element={<Login />} />
      </>
    );
  } else {
    userRoutes = (
      <>
        <Route path="/logout" element={<Logout />} />
        <Route path="/login"  element={<Login />} />
        <Route path = "/games/${gameId}/spectate" element = {<PrivateRoute><JoinGame /></PrivateRoute>} />          

      </>
    );
  }

  return (
    <div>
      <ErrorBoundary FallbackComponent={ErrorFallback}>
        <AppNavbar />
        <Routes>
          <Route path="/"      exact={true} element={<Home />} />
          <Route path="/plans"               element={<PlanList />} />
          <Route path="/docs"                element={<SwaggerDocs />} />
          {publicRoutes}
          {userRoutes}
          {adminRoutes}
          {ownerRoutes}
          {vetRoutes}
          <Route path="*" element={<Home />} />
        </Routes>
      </ErrorBoundary>
    </div>
  );
}

export default App;