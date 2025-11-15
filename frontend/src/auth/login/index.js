import React, { useState } from "react";
import { Alert } from "reactstrap";
import FormGenerator from "../../components/formGenerator/formGenerator";
import tokenService from "../../services/token.service";
import "../../static/css/auth/authButton.css";
import { loginFormInputs } from "./form/loginFormInputs";

export default function Login(props) {
  const [message, setMessage] = useState(props?.message || null)
  const loginFormRef = React.createRef();      
  

  async function handleSubmit({ values }) {

    const reqBody = values;
    setMessage(null);
    await fetch("/api/v1/auth/signin", {
      headers: { "Content-Type": "application/json" },
      method: "POST",
      body: JSON.stringify(reqBody),
    })
      .then(function (response) {
        if (response.status === 200) return response.json();
        else return Promise.reject("Invalid login attempt");
      })
      .then(function (data) {
        tokenService.setUser(data);
        tokenService.updateLocalAccessToken(data.token);

        const params = new URLSearchParams(window.location.search);
        const back = params.get("return");
        const roles = Array.isArray(data.roles) ? data.roles : [];

        let fallback = "/";
        if (roles.includes("PLAYER")) {
          fallback = "/create-game";
        } else if (roles.includes("ADMIN")) {
          fallback = "/users";
        }

        window.location.href = back || fallback;
      })
      .catch((error) => {         
        setMessage(error);
      });            
  }

  
    return (
      <div className="auth-page-container">
        {message ? (
          <Alert color="primary">{message}</Alert>
        ) : (
          <></>
        )}

        <h1 className="title-xl">LOGIN</h1>

        <div className="auth-form-container">
          <FormGenerator
            ref={loginFormRef}
            inputs={loginFormInputs}
            onSubmit={handleSubmit}
            numberOfColumns={1}
            listenEnterKey
            buttonText="Login"
            buttonClassName="btn"
          />
        </div>
      </div>
    );  
}