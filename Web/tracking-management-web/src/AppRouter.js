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


class AppRouter extends React.Component {
  render() {
    return (
      <div>
        <Router>
          <div>
            <Switch>
              <Route path="/login">
                <Login />
              </Route>
              <Route path="/signup">
                <SignUp />
              </Route>
              <Route path="/adddevice">
                <AddDevice />
              </Route>
              <Route path="/">
                <App />
              </Route>
            </Switch>
          </div>
        </Router>
      </div>
    );
  }
}

export default AppRouter;
