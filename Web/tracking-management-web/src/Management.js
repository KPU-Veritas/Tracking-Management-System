import React from "react";
import {
    Button,
    Grid,
  } from "@material-ui/core";
import { call } from "./service/ApiService";

class Managerment extends React.Component {
    constructor(props) {
        super(props);
        this.state = { warningLevel : null};
    }

    componentDidMount() {
        call("/system/getlevel", "GET", null).then((response) =>
        this.setState({ warningLevel: response})
        );
    }


    onChangeHandler = (e) => {
        this.setState({ warningLevel : e.target.value});
    }

    setLevel() {
        call("/system/setlevel", "POST", {id : null,  warningLevel : this.state.warningLevel} ).then((response) =>
        this.setState()
        );
    }

    render() {
        const level = this.state.warningLevel;
        this.setLevel = this.setLevel.bind(this);
        return (
        <><Grid>
            <form>
            <input        
            name="password"
            value={level}
            onChange={this.onChangeHandler}/>
            </form><p>현재 위험도{level}% 까지 알림이 갑니다.</p>
            </Grid>
            <Grid><Button onClick={this.setLevel}>확인</Button></Grid></>
        

        );
    }
}

export default Managerment;