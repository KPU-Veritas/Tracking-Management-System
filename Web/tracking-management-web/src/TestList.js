import React from "react";


class TestList extends React.Component {
    constructor(props) {
        super(props);
        this.state = { contact: props.contact };
    }

    render() {
        const contact = this.state.contact;
        return (
        <>
            <table id="list" align="center">
            <thead>
                <tr>
                    <th>이용자 UUID</th>
                    <th>접촉자 UUID</th>
                    <th>접촉일자</th>
                    <th>첫 접촉시각</th>
                    <th>마지막 접촉시각</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>{contact.uuid}</td>
                    <td>{contact.contactTargetUuid}</td>
                    <td>{contact.date}</td>
                    <td>{contact.firstTime}</td>
                    <td>{contact.lastTime}</td>
                </tr>
            </tbody>
        </table>
    </>          
        
        );
    }
}

export default TestList;