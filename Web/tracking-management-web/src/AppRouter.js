import React from "react";
import "./index.css";
import App from "./App";
import UserList from "./UserList";
import Login from "./Login";
import SignUp from "./SignUp";
import { BrowserRouter as Router, Switch, Route} from "react-router-dom";
import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";
import AddDevice from "./AddDevice";
import AddInfected from "./AddInfected";
import ModifyUser from "./ModifyUser";
import Notification from "./Notification";


class AppRouter extends React.Component {
  render() {
    return (
      <div>
        <Router>
          <div>
            <Switch>
              <Route path="/signup">
                <SignUp />
              </Route>
              <Route path="/adddevice">
                <AddDevice />
              </Route>
              <Route path="/addinfected/:uuid">
                <AddInfected />
              </Route>
              <Route path="/modifyuser/:uuid/:email/:userName/:phoneNumber/:simpleAddress/:detailAddress">
                <ModifyUser />
              </Route>
              <Route path="/main">
                <App />
              </Route>
              <Route path="/notification/:email">
                <Notification />
              </Route>
              <Route path="/">
                <Login />
              </Route>
            </Switch>
          </div>
        </Router>
      </div>
    );
  }
}

export default AppRouter;
