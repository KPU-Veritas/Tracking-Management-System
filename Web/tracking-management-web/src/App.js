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
import UserList from "./UserList";

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      userList: []
    };
  }

  componentDidMount() {
    call("/auth/userlist", "GET", null).then((response) =>
      this.setState({ userList: response.data})
    );
  }
  
  render() {
    var userList = this.state.userList.length > 0 && (
      <Paper style={{ margin: 16 }}>
        <List>
          {this.state.userList.map((user, idx) => (
            <UserList
              user={user}
              key={user.uuid}
            />
          ))}
        </List>
      </Paper>
    );

    var navigationBar = (
      <AppBar position="static">
        <Toolbar>
          <Grid justifyContent="space-between" container>
            <Grid>
              <Button color="inherit" onClick={signout}>
                로그아웃
              </Button>
            </Grid>
          </Grid>
        </Toolbar>
      </AppBar>
    );

    var userListPage = (
      <div>
        <h1>회원목록</h1>
        {navigationBar}
        <Container maxWidth="md">
          <div className="UserList">{userList}</div>
        </Container>
      </div>
    );

    var content = userListPage;
    
    return <div className="App">{content}</div>;
  }
}

export default App;
