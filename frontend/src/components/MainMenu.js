import React from 'react';
import { useNavigate } from 'react-router-dom';
import './MainMenu.css';

// Import Assets
import nexusLogo from '../assets/logo.svg';
// Use your specific background image here.
// If you don't have 'menu-bg.png', you can swap this back to 'chart-graph.png'
import backgroundTexture from '../assets/menu-bg.png';

const MainMenu = () => {
    const navigate = useNavigate();

    return (
        <div className="menu-page">

            {/* LAYER 1: BACKGROUND TEXTURE */}
            <img src={backgroundTexture} alt="Background" className="menu-bg-texture" />

            {/* LAYER 2: CONTENT */}
            <div className="menu-content">

                {/* Logo */}
                <img src={nexusLogo} alt="SNeXus Logo" className="menu-logo" />

                {/* Main Text */}
                <h1 className="welcome-title">Welcome to neXus!</h1>

                <p className="welcome-desc">
                    You will be playing in a highly volatile market against
                    automated traders. <br />
                    <span className="highlight-text">Choose your stocks wisely!</span>
                </p>

                {/* Button */}
                <button
                    onClick={() => navigate('/dashboard')}
                    className="start-btn-large"
                >
                    Start now!
                </button>

                {/* Disclaimer (Kept as requested) */}
                <p className="disclaimer-text">
                    *If you go bankrupt ($0), the bank will automatically bail you out.
                </p>

            </div>
        </div>
    );
};

export default MainMenu;