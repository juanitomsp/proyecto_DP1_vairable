import { useState } from "react";
import { Link } from "react-router-dom";
import { Table } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import deleteFromList from "../../util/deleteFromList";
import getErrorModal from "../../util/getErrorModal";
import useFetchState from "../../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

export default function UserListAdmin() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [users, setUsers] = useFetchState(
    [],
    `/api/v1/users`,
    jwt,
    setMessage,
    setVisible
  );
  const [alerts, setAlerts] = useState([]);

  const userRows = users.map((user) => (
    <tr key={user.id}>
      <td className="text-center" style={{color:'white'}}>{user.username}</td>
      <td className="text-center" style={{color:'white'}}>{user.email}</td>
      <td className="text-center" style={{color:'white'}}>{user.authority.authority}</td>
      <td className="text-center" style={{color:'white'}}>
        <div className="admin-actions">
          <Link
            className="btn"
            aria-label={`edit-${user.id}`}
            to={`/users/${user.id}`}
          >
            EDIT
          </Link>
          <button
            className="btn btn-danger"
            aria-label={`delete-${user.id}`}
            onClick={() =>
              deleteFromList(
                `/api/v1/users/${user.id}`,
                user.id,
                [users, setUsers],
                [alerts, setAlerts],
                setMessage,
                setVisible
              )
            }
          >
            DELETE
          </button>
        </div>
      </td>
    </tr>
  ));

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <main className="admin-page-container">
      <header className="admin-header">
        <h1 className="title-xl">USERS</h1>
      </header>

      {alerts.map((a) => a.alert)}
      {modal}

      <section className="admin-panel">
        <div className="admin-actions">
          <Link className="btn" to="/users/new">ADD USER</Link>
        </div>

        <Table aria-label="users" className="admin-table">
          <thead>
            <tr>
              <th className="text-center" style={{color:'white'}}>Username</th>
              <th className="text-center" style={{color:'white'}}>Email</th>
              <th className="text-center" style={{color:'white'}}>Authority</th>
              <th className="text-center" style={{color:'white'}}>Actions</th>
            </tr>
          </thead>
          <tbody>{userRows}</tbody>
        </Table>
      </section>
    </main>
  );
}