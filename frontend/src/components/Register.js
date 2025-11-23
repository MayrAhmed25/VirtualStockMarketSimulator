import React, { useState } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';
import './Register.css';

// IMPORT THE LOGO
import nexusLogo from '../assets/logo.svg';

const Register = () => {
    // --- LOGIC SECTION ---
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            // POST request to Register endpoint (No email, just user/pass)
            await api.post('/auth/register', { username, password });

            alert("Account Created Successfully! Please Login.");
            navigate('/login');
        } catch (error) {
            alert("Registration Failed! Username might be taken.");
        }
    };

    // --- DESIGN SECTION ---
    return (
        <div className="register-container">

            {/* LEFT SIDE: Text */}
            <div className="left-section">
                <img src={nexusLogo} alt="Nexus Logo" className="logo-img" />

                <p className="welcome-text">
                    Join the Nexus community today. Start your journey into the
                    world of simulated trading and master the markets risk-free.
                </p>
                <p className="tagline">Your profitable future starts here.</p>
            </div>

            {/* RIGHT SIDE: The Registration Form */}
            <div className="right-section">
                <div className="register-box">
                    <h2>Create Account</h2>

                    <form onSubmit={handleRegister}>
                        <div className="input-group">
                            <label>Username</label>
                            <input
                                type="text"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                            />
                        </div>

                        <div className="input-group">
                            <label>Password</label>
                            <input
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>

                        <button type="submit" className="register-btn">
                            Sign Up
                        </button>
                    </form>

                    <p className="login-link">
                        Already have an account?{' '}
                        <span onClick={() => navigate('/login')}>
                            Login here
                        </span>
                    </p>
                </div>
            </div>

        </div>
    );
};

export default Register;