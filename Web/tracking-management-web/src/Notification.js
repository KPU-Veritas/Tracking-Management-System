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
import { tableBodyClasses } from "@mui/material";

class Notification extends React.Component {
  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
  }


  handleSubmit(event) {
    event.preventDefault();
    const data = new FormData(event.target);
    const title = data.get("title");
    const body = data.get("body");
    const email = this.props.match.params.email;
    
    call("/system/notificateindividual", "PUT", {email : email, title : title, body : body}).then(
      (response) => {
        window.location.href = "/main";
        alert("경고 전송이 완료되었습니다.");
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
                경고 알림 전송
              </Typography>
            </Grid>
            <Grid item xs={12}>
              <TextField
                autoComplete="title"
                name="title"
                variant="outlined"
                required
                fullWidth
                id="title"
                label="제목"
                autoFocus
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                autoComplete="body"
                name="body"
                variant="outlined"
                fullWidth
                id="body"
                label="내용"
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

export default withRouter(Notification);
