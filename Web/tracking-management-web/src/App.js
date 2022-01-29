import React from "react";
import "./App.css";
import {
  Paper,
  List,
  Container,
  Grid,
  Button,
  AppBar,
  Toolbar,
  Typography,
} from "@material-ui/core";
import { call, signout } from "./service/ApiService"; // signout 추가

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      items: [],
      /* 1. 로딩중이라는 상태이다. 생성자에 상태 변수를 추가한다. */
      loading: true,
    };
  }

  render() {
    
    return <div className="App"></div>;
  }
}

export default App;
