import React from "react";
import {
  Button,
  TextField,
  Link,
  Grid,
  Container,
  Typography,
} from "@material-ui/core";
import {call} from "./service/ApiService";
import DatePicker from "react-datepicker";
import moment from 'moment';
import { withRouter } from "react-router-dom";

class ModifyUser extends React.Component {
  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
  }


  handleSubmit(event) {
    event.preventDefault();
    const data = new FormData(event.target);
    const email = data.get("email");
    const password = data.get("password");
    const username = data.get("username");
    const simpleAddress = data.get("simpleAddress");
    const phoneNumber = data.get("phoneNumber");
    const uuid = this.props.match.params.uuid;
    console.log(data);
    call("/system/modifyuser", "PUT", { uuid : uuid, username : username, email : email, password : password,  phoneNumber : phoneNumber, simpleAddress : simpleAddress }).then(
      (response) => {
        window.location.href = "/";
        alert("회원정보가 수정되었습니다.");
      }
    );
  }
  

  render() {
    return (
      <Container component="main" maxWidth="xs" style={{ marginTop: "8%" }}>
        <form noValidate onSubmit={this.handleSubmit}>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <Typography component="h1" variant="h5">
                회원정보 수정
              </Typography>
            </Grid>
            <Grid item xs={12}>
              <TextField
                autoComplete="email"
                name="email"
                variant="outlined"
                required
                fullWidth
                id="email"
                label="이메일"
                defaultValue={this.props.match.params.email}
                autoFocus
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                autoComplete="password"
                name="password"
                variant="outlined"
                fullWidth
                id="password"
                label="비밀번호"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                autoComplete="name"
                name="username"
                variant="outlined"
                required
                fullWidth
                id="username"
                label="이름"
                defaultValue={this.props.match.params.userName}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                autoComplete="address"
                name="simpleAddress"
                variant="outlined"
                required
                fullWidth
                id="simpleAddress"
                label="주소"
                defaultValue={this.props.match.params.simpleAddress}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                autoComplete="phonenumber"
                name="phoneNumber"
                variant="outlined"
                required
                fullWidth
                id="phoneNumber"
                label="전화번호"
                defaultValue={this.props.match.params.phoneNumber}
              />
            </Grid>
            <Grid item xs={12}>
              <Button
                type="submit"
                fullWidth
                variant="contained"
                color="primary"
              >
                확인
              </Button>
            </Grid>
          </Grid>
        </form>
      </Container>
    );
  }
}

export default withRouter(ModifyUser);
