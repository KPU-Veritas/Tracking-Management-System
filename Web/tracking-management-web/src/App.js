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
import Greeting from "./Greeting";
import ContactList from "./ContactList";

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      userList: [],
      contactList: [],
      page: 1,
    };
  }

  componentDidMount() {
    call("/system/userlist", "GET", null).then((response) =>
      this.setState({ userList: response.data})
    );
    call("/system/contactlist", "GET", null).then((response) =>
      this.setState({ contactList: response.data})
    );
  }

  mainPage = () => {
    this.setState({
      page: 1,
    });
  };

  userList = () => {
    this.setState({
      page: 2,
    });
  };

  contactList = () => {
    this.setState({
      page: 3,
    });
  };
  
  render() {
    var greeting = 
      <Paper style={{ margin: 16 }}>
            <Greeting/>
      </Paper>;

    var userList = this.state.userList.length > 0 && (
      <Paper style={{ margin: 16 }}>
        <List>
          {this.state.userList.map((user) => (
            <UserList
              user={user}
              key={user.uuid}
            />
          ))}
        </List>
      </Paper>
    );

    var contactList = this.state.contactList.length > 0 && (
      <Paper style={{ margin: 16 }}>
        <List>
          {this.state.contactList.map((contact, idx) => (
            <ContactList
              contact={contact}
              key={idx}
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
              <Button color="inherit" onClick={this.mainPage}>
                메인화면
              </Button>
              <Button color="inherit" onClick={this.userList}>
                회원목록
              </Button>
              <Button color="inherit" onClick={this.contactList}>
                접촉목록
              </Button>
            </Grid>
          </Grid>
        </Toolbar>
      </AppBar>
    );

    var mainPage = (
      <div>
        <h1>메인화면</h1>
        {navigationBar}
        <Container maxwitdth="md">
          <div className="Greeting">{greeting}</div>
        </Container>
      </div>
    )

    var userListPage = (
      <div>
        <h1>회원목록</h1>
        {navigationBar}
        <Container maxwitdth="md">
          <div className="UserList">{userList}</div>
        </Container>
      </div>
    );

    var contactListPage = (
      <div>
        <h1>접촉목록</h1>
        {navigationBar}
        <Container maxwitdth="md">
          <div className="ContactList">{contactList}</div>
        </Container>
      </div>
    );
    
    var content;

    switch (this.state.page) {
      case 1: content = mainPage; break;
      case 2: content = userListPage; break;
      case 3: content = contactListPage; break;
    }

    
    return <div className="App">{content}</div>;
  }
}

export default App;
