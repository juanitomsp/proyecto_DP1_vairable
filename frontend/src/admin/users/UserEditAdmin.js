import { useState } from "react";
import { Link } from "react-router-dom";
import { Form, Input, Label } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import getErrorModal from "../../util/getErrorModal";
import getIdFromUrl from "../../util/getIdFromUrl";
import useFetchData from "../../util/useFetchData";
import useFetchState from "../../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

export default function UserEditAdmin() {
  const emptyItem = { id: null, username: "", password: "", authority: null };
  const id = getIdFromUrl(2);

  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [user, setUser] = useFetchState(
    emptyItem,
    `/api/v1/users/${id}`,
    jwt,
    setMessage,
    setVisible,
    id
  );
  const auths = useFetchData(`/api/v1/users/authorities`, jwt);

  function handleChange(event) {
    const { name, value } = event.target;
    if (name === "authority") {
      const auth = auths.find((a) => a.id === Number(value));
      setUser({ ...user, authority: auth });
    } else {
      setUser({ ...user, [name]: value });
    }
  }

  function handleSubmit(event) {
    event.preventDefault();

    fetch("/api/v1/users" + (user.id ? "/" + user.id : ""), {
      method: user.id ? "PUT" : "POST",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(user),
    })
      .then((response) => response.json())
      .then((json) => {
        if (json.message) {
          setMessage(json.message);
          setVisible(true);
        } else {
          window.location.href = "/users";
        }
      })
      .catch((message) => alert(message));
  }

  const modal = getErrorModal(setVisible, visible, message);
  const authOptions = auths.map((auth) => (
    <option key={auth.id} value={auth.id}>
      {auth.authority}
    </option>
  ));

  return (
    <main className="admin-page-container">
      <header className="admin-header">
        <h1 className="title-xl">{user.id ? "EDIT USER" : "ADD USER"}</h1>
      </header>

      {modal}

      <section className="admin-panel">
        <Form className="form" onSubmit={handleSubmit}>
          <div className="form-field">
            <Label for="username" className="label">USERNAME</Label>
            <Input
              type="text"
              required
              name="username"
              id="username"
              value={user.username || ""}
              onChange={handleChange}
              className="input"
            />
          </div>

          <div className="form-field">
            <Label for="password" className="label">PASSWORD</Label>
            <Input
              type="password"
              required
              name="password"
              id="password"
              value={user.password || ""}
              onChange={handleChange}
              className="input"
            />
          </div>
          <div className="form-field">
            <Label for="email" className="label">EMAIL</Label>
            <Input
              type="email"
              required
              name="email"
              id="email"
              value={user.email || ""}
              onChange={handleChange}
              className="input"
            />
          </div>

          <div className="form-field">
            <Label for="authority" className="label">AUTHORITY</Label>
            <Input
              type="select"
              name="authority"
              id="authority"
              value={user.authority?.id || ""}
              onChange={handleChange}
              className="input"
              disabled={!!user.id}
              required
            >
              <option value="">None</option>
              {authOptions}
            </Input>
          </div>

          <div className="admin-actions">
            <button className="btn" type="submit">SAVE</button>
            <Link className="btn" to="/users">CANCEL</Link>
          </div>
        </Form>
      </section>
    </main>
  );
}