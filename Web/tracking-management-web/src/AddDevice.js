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

class AddDevice extends React.Component {
  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit(event) {
    event.preventDefault();
    const data = new FormData(event.target);
    const place = data.get("place");
    call("/system/adddevice", "POST", { place : place }).then(
      (response) => {
        window.location.href = "/main";
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
                장치 추가
              </Typography>
            </Grid>
            <Grid item xs={12}>
              <TextField
                autoComplete="place"
                name="place"
                variant="outlined"
                required
                fullWidth
                id="place"
                label="설치 장소"
                autoFocus
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

export default AddDevice;
