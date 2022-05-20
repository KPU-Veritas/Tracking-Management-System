import React from "react";
import {
    Button,
    Grid,
  } from "@material-ui/core";
import { call } from "./service/ApiService";

class Managerment extends React.Component {
    constructor(props) {
        super(props);
        this.state = { warningLevel : 0};
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
        if(this.state.warningLevel <= 100 && this.state.warningLevel > 0) {
            call("/system/setlevel", "POST", {id : null,  warningLevel : this.state.warningLevel} ).then((response) =>
            this.setState()
            );
            alert("자동 알림을 전송할 위험도를 설정했습니다.");
        }
        else {
            alert("입력한 값이 올바르지 않습니다.");
        }
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
            </form><p>현재 위험도 {level}% 까지 알림이 갑니다.</p>
            </Grid>
            <Grid><Button onClick={this.setLevel}>확인</Button></Grid></>
        

        );
    }
}

export default Managerment;