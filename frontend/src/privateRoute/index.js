import React, { useState } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import tokenService from '../services/token.service';

const PrivateRoute = ({ children }) => {
    const location = useLocation();
    const jwt = tokenService.getLocalAccessToken();
    const [isLoading, setIsLoading] = useState(true);
    const [isValid, setIsValid] = useState(null);

    if (jwt) {
        fetch(`/api/v1/auth/validate?token=${jwt}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
        }).then(response => {
            return response.json();
        }).then(isValid => {
            setIsValid(isValid);
            setIsLoading(false);
        });
    } else {
        // redirect to login with return param
        return <Navigate to={`/login?return=${encodeURIComponent(location.pathname)}`} replace />;
    }

    if (isLoading === true) {
        return <div>Loading...</div>;
    } else return isValid === true ? children : <Navigate to={`/login?return=${encodeURIComponent(location.pathname)}`} replace />
};

export default PrivateRoute;