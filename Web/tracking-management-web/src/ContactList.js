import React from "react";


class ContactList extends React.Component {
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
                <tr align="left">
                    <th>이용자 UUID&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;</th>
                    <th>접촉자 UUID&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;</th>
                    <th>접촉일자&emsp;&emsp;</th>
                    <th>첫 접촉시각&emsp;&emsp;</th>
                    <th>마지막 접촉시각&emsp;&emsp;</th>
                </tr>
            </thead>
            <tbody>
                <tr align="left">
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

export default ContactList;