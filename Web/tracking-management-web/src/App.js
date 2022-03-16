import React, { useState, Component } from "react";
import "./App.css";
import {
  Paper,
  List,
  Container,
  Grid,
  Button,
  AppBar,
  Toolbar,
  InputBase,
} from "@material-ui/core";
import Badge from '@mui/material/Badge';
import MailIcon from '@mui/icons-material/Mail';
import { FcSettings } from 'react-icons/fc';
import { call, signout, } from "./service/ApiService"; // signout 추가
import UserList from "./UserList";
import Greeting from "./Greeting";
import ContactList from "./ContactList";
import InfectedList from "./InfectedList";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import moment from 'moment';
import Managerment from './Management';

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      userList: [],
      contactList: [],
      infectedList: [],
      page: 1,
      notice : 0,
      search : null,
      searchDate : new Date(),
      endDate : new Date()
    };
  }

  componentDidMount() {
    this.updateCount = this.updateCount.bind(this);
    //setInterval(this.updateCount, 1000);
  }

  updateCount() {
    call("/system/notice", "GET", null).then((response) => {
        this.setState({ notice: response })
    }
    );
  }

  editEventHandler = (e) => {
    this.setState({ search: e.target.value });
  };

  getUserData() {
    call("/system/userlist", "GET", null).then((response) =>
    this.setState({ userList: response.data})
    );
  }

  getContactData() {
    call("/system/contactlist", "GET", null).then((response) =>
    this.setState({ contactList: response.data})
    );
  }

  getNoticeData() {
    call("/system/noticelist", "GET", null).then((response) =>
      this.setState({ noticeList: response.data})
    );
  }   //현재사용되지않음

  getInfectedData() {
    call("/system/noticelist", "GET", null).then((response) =>
      this.setState({ infectedList: response.data})
   );
  }

  searchUser = (e) => {
    if (e.key === "Enter") {
      call("/system/searchuser", "POST", this.state.search ).then((response) =>
      this.setState({ userList: response.data, search : null})
      );
    }
  }

  searchContact = (e) => {
    if (e.key === "Enter") {
      var moment = require('moment');
      const date = moment(this.state.searchDate).format('YY-MM-DD');
      const date2 = moment(this.state.endDate).format('YY-MM-DD');
      call("/system/searchcontact", "POST", { date : date, date2 : date2, uuid : this.state.search } ).then((response) =>
      this.setState({ contactList: response.data, search : null, searchDate : new Date(), endDate : new Date()})
      );
    }
  }

  mainPage = () => {
    this.setState({
      page: 1,
    });
  };

  userList = () => {
    this.getUserData();
    this.setState({
      page: 2,
    });
  };

  contactList = () => {
    this.getContactData();
    this.setState({
      page: 3,
    });
  };

  noticeList = () => {
    this.getInfectedData();
    this.setState({
      page: 4,
    });
  };

  management = () => {
    this.setState({
      page: 5,
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
              key={user.id}
            />
          ))}
        </List>
        <InputBase
            inputProps={{
              "aria-label": "naked",
            }}
            type="text"
            value={this.state.search}
            fullWidth={true}
            onChange={this.editEventHandler}
            onKeyPress={this.searchUser}
          />
      </Paper>
    );

    var contactList = this.state.contactList.length > 0 && (
      <Paper style={{ margin: 16 }}>
        <List>
          {this.state.contactList.map((contact) => (
            <ContactList
              contact={contact}
              key={contact.id}
            />
          ))}
        </List>
        <p>시작 날짜</p>
        <DatePicker
        selectRange={true}
        selected={this.state.searchDate}
        onChange={(date) =>
          this.setState({
            searchDate: date,
          })
        } />
        <p>끝 날짜</p>
        <DatePicker
        selectRange={true}
        selected={this.state.endDate}
        onChange={(date) =>
          this.setState({
            endDate: date,
          })
        } />
        <p>uuid 입력 : </p>
        <InputBase
            inputProps={{
              "aria-label": "naked",
            }}
            type="text"
            value={this.state.search}
            fullWidth={true}
            onChange={this.editEventHandler}
            onKeyPress={this.searchContact}
          />
      </Paper>
    );

    var infectedList = this.state.infectedList.length > 0 && (
      <Paper style={{ margin: 16 }}>
        <List>
          {this.state.infectedList.map((notice, idx) => (
            <InfectedList
              notice={notice}
              key={idx}
            />
          ))}
        </List>
      </Paper>
    );

    var management = (
      <Paper style={{ margin: 16}}>
        <Managerment></Managerment>
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
              <Button onClick={this.noticeList}>
              <Badge badgeContent={this.state.notice} color="primary">
                <MailIcon color="action" />
              </Badge>
              </Button>
              <Button onClick={this.management}>
              <FcSettings>
              </FcSettings>
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

    var infectedListPage = (
      <div>
        <h1>확진알림</h1>
        {navigationBar}
        <Container maxwitdth="md">
          <div className="InfectedList">{infectedList}</div>
        </Container>
      </div>
    );

    var managementPage = (
      <div>
        <h1>관리창</h1>
        {navigationBar}
        <Container maxwitdth="md">
          <div className="Management">{management}</div>
        </Container>
      </div>
    );

    
    var content;

    switch (this.state.page) {
      case 1: content = mainPage; break;
      case 2: content = userListPage; break;
      case 3: content = contactListPage; break;
      case 4: content = infectedListPage; break;
      case 5: content = managementPage; break;
    }

    
    return <div className="App">{content}</div>;
  }
}

export default App;
