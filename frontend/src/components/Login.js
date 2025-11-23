import React, { useState } from 'react';
import api from '../services/api'; // Ensure this path is correct for your project
import { useNavigate } from 'react-router-dom';
import './Login.css'; // Connects the design

// IMPORT THE LOGO
import nexusLogo from '../assets/logo.svg';

const Login = () => {
    // --- LOGIC SECTION ---
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post('/auth/login', { username, password });
            localStorage.setItem('token', response.data);
            navigate('/menu');
        } catch (error) {
            alert("Login Failed! Check credentials.");
        }
    };

    // --- DESIGN SECTION ---
    return (
        <div className="login-container">

            {/* LEFT SIDE: Logo & Text */}
            <div className="left-section">
                {/* The Logo Image */}
                <img src={nexusLogo} alt="Nexus Logo" className="logo-img" />

                <p className="welcome-text">
                    Welcome to Nexus, first of its kind online trading simulation.
                    We want to empower all students of trade and help them access
                    real time simulation of trades.
                </p>
                <p className="tagline">Ready to unlock your profitable future?</p>
            </div>

            {/* RIGHT SIDE: The Login Form */}
            <div className="right-section">
                <div className="login-box">
                    <h2>Login</h2>

                    <form onSubmit={handleLogin}>
                        <div className="input-group">
                            <label>Username</label>
                            <input
                                type="text"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                placeholder=""
                            />
                        </div>

                        <div className="input-group">
                            <label>Password</label>
                            <input
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder=""
                            />
                        </div>

                        <button type="submit" className="login-btn">
                            Login
                        </button>
                    </form>

                    <p className="register-link">
                        New to SNeXus?{' '}
                        <span onClick={() => navigate('/register')}>
                            Create Account
                        </span>
                    </p>
                </div>
            </div>

        </div>
    );
};

export default Login;