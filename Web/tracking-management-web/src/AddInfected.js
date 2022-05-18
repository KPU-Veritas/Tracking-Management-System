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

class AddInfected extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
        searchDate : new Date(),
    }
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit(event) {
    event.preventDefault();
    const data = new FormData(event.target);
    const detailSituation = data.get("detailSituation");
    const estimatedDate = moment(this.state.searchDate).format('YY-MM-DD');
    const uuid = this.props.match.params.uuid;
    let now = new Date();
    now = moment(now).format('YY-MM-DD');
    call("/system/addinfected", "POST", { uuid : uuid, judgmentDate : now, estimatedDate : estimatedDate, detailSituation : detailSituation }).then(
      (response) => {
        window.location.href = "/main";
        alert("확진 등록과 함께 자동 경고 알림이 전송되었습니다.");
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
                확진자 추가
              </Typography>
            </Grid>
            <Grid item xs={12}>
              <TextField
                autoComplete="detailSituation"
                name="detailSituation"
                variant="outlined"
                required
                fullWidth
                id="detailSituation"
                label="상세 행위"
                autoFocus
              />
            </Grid>
            <Grid item xs={12}>
                <DatePicker
                selected={this.state.searchDate}
                onChange={(date) =>
                    this.setState({
                        searchDate: date,
                    })
                }
                required
                fullWidth
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

export default withRouter(AddInfected);
