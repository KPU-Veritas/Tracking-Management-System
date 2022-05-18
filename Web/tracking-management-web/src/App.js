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
import { call, signout, } from "./service/ApiService";
import { Link } from 'react-router-dom';
import Pagination from "react-js-pagination";
import UserList from "./UserList";
import Greeting from "./Greeting";
import ContactList from "./ContactList";
import InfectedList from "./InfectedList";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import moment from 'moment';
import Managerment from './Management';
import DeviceManagement from './DeviceManagement';
import { SecurityUpdateGood } from "@mui/icons-material";

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      userList: [],
      contactList: [],
      infectedList: [],
      deviceList: [],
      page: 1,
      activePage: 1,
      totalItem: 1,
      notice : 0,
      search : null,
      searchDate : new Date(),
      endDate : new Date(),
    };
    this.searchUser = this.searchUser.bind(this);
    this.searchContact = this.searchContact.bind(this);
    this.updateCount = this.updateCount.bind(this);
  }

  componentDidMount() {
    setInterval(this.updateCount, 1000);
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
    this.setState({ userList : 0 });
    call("/system/userlist", "POST", this.state.activePage).then((response) =>
    this.setState({ userList: response.data})
    );
  }

  getContactData() {
    this.setState({ contactList : 0 })
    call("/system/contactlist", "POST", this.state.activePage).then((response) =>
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

  getDeviceData() {
    call("/system/devicelist", "GET", null).then((response) =>
      this.setState({ deviceList: response.data})
    );
  }
  

  searchUserKeyboard = (e) => {
    if (e.key === "Enter") {
      this.searchUser();
    }
  }

  searchUser() {
      this.setState({ userList : 0 })
      call("/system/searchuser", "POST", this.state.search ).then((response) =>
      this.setState({ userList: response.data, search : null, activePage : 1, totalItem : 1})
      );
  }

  searchContactKeyboard = (e) => {
    if (e.key === "Enter") {
      this.searchContact();
    }
  }

  searchContact() {
    this.setState({ contactList : 0})
    var moment = require('moment');
    const date = moment(this.state.searchDate).format('YY-MM-DD');
    const date2 = moment(this.state.endDate).format('YY-MM-DD');
    call("/system/searchcontact", "POST", { date : date, date2 : date2, uuid : this.state.search } ).then((response) =>
    this.setState({ contactList: response.data, search : null, searchDate : new Date(), endDate : new Date(), activePage : 1, totalItem : 1})
    );
  }

  handleUserPageChange = (nextPage) => {
    this.setState({ activePage : nextPage }, () => {this.getUserData();})
  }

  handleContactPageChange = (nextPage) => {
    this.setState({ activePage : nextPage }, () => {this.getContactData();})
  }

  mainPage = () => {
    this.setState({
      page: 1,
    });
  };

  userList = () => {
    call("/system/totaluser", "GET", null).then((response) =>
    this.setState({ totalItem: response })
    );
    this.setState({
      page: 2,
      activePage: 1,
    }, () => {this.getUserData();})
  };

  contactList = () => {
    call("/system/totalcontact", "GET", null).then((response) =>
    this.setState({ totalItem: response})
    );
    this.setState({
      page: 3,
      activePage: 1,
    }, () => {this.getContactData();})
  };

  deviceManagement = () => {
    this.getDeviceData();
    this.setState({
      page: 4,
    })
  }

  noticeList = () => {
    this.getInfectedData();
    this.setState({
      page: 5,
    });
  };

  management = () => {
    this.setState({
      page: 6,
    });
  };
  
  render() {

    var greeting = 
      <Paper style={{ margin: 16 }}>
            <Greeting/>
      </Paper>;

    var userList = this.state.userList.length > 0 && (
      <Paper style={{ margin: 16 }}>
        <input 
            inputprops={{
              "aria-label": "naked",
            }}
            type="text"
            value={this.state.search|| ''}
            fullwidth="true"
            onChange={this.editEventHandler}
            onKeyPress={this.searchUserKeyboard}></input><button name="search" onClick={this.searchUser}>검색</button> 

        <List>
          {this.state.userList.map((user, idx) => (
            <UserList
              user={user}
              key={idx}
            />
          ))}
        </List>
        <Pagination activePage={this.state.activePage}
          itemsCountPerPage={10}
          totalItemsCount={this.state.totalItem}
          pageRangeDisplayed={5}
          prevPageText={"‹"}
          nextPageText={"›"}
          onChange={this.handleUserPageChange.bind(this)} />

      </Paper>
    );

    var contactList = this.state.contactList.length > 0 && (
      <Paper style={{ margin: 16 }}>
        <p>시작 날짜</p>
        <DatePicker
        variant="outlined"
        selected={this.state.searchDate}
        onChange={(date) =>
          this.setState({
            searchDate: date,
          })
        } />
        <p>끝 날짜</p>
        <DatePicker
        variant="outlined"
        selected={this.state.endDate}
        onChange={(date) =>
          this.setState({
            endDate: date,
          })
        } />
        <p>uuid 입력</p>
        <input 
            inputprops={{
              "aria-label": "naked",
            }}
            type="text"
            value={this.state.search|| ''}
            fullwidth="true"
            onChange={this.editEventHandler}
            onKeyPress={this.searchContactKeyboard}></input><button name="search" onClick={this.searchContact}>검색</button> 

        <List>
          {this.state.contactList.map((contact) => (
            <ContactList
              contact={contact}
              key={contact.id}
            />
          ))}
        </List>

        <Pagination activePage={this.state.activePage}
          itemsCountPerPage={10}
          totalItemsCount={this.state.totalItem}
          pageRangeDisplayed={5}
          prevPageText={"‹"}
          nextPageText={"›"}
          onChange={this.handleContactPageChange.bind(this)} />

      </Paper>
    );

    var deviceManagement =
      <Paper style={{ margin: 16 }}>
        <List>
          {this.state.deviceList.map((device, idx) => (
            <DeviceManagement
              device={device}
              key={idx}
            />
          ))}
        </List>
        <Grid>
          <Link to="./adddevice">
            <Button>장치추가</Button>
          </Link>
        </Grid>
      </Paper>

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
              <Button color="inherit" onClick={this.deviceManagement}>
                장치관리
              </Button>
              <Button color="inherit" onClick={this.deviceManagement}>
                {this.state.activePage}
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

    var deviceManagementPage = (
      <div>
        <h1>장치관리</h1>
        {navigationBar}
        <Container maxwitdth="md">
          <div className="DeviceManagement">{deviceManagement}</div>
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
      case 4: content = deviceManagementPage; break;
      case 5: content = infectedListPage; break;
      case 6: content = managementPage; break;
    }

    
    return <div className="App">{content}</div>;
  }
}

export default App;
