import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Landing.css';

// --- ASSETS ---
// Ensure these are in your src/assets folder
import nexusLogo from '../assets/logo.svg';
import chartGraph from '../assets/chart-graph.png';

const Landing = () => {
    const navigate = useNavigate();

    return (
        <div className="landing-page">

            {/* 1. LOGO HEADER (Overlaps the box) */}
            <div className="logo-header">
                <img src={nexusLogo} alt="Nexus Logo" className="top-logo" />
            </div>

            {/* 2. THE NEON BOX */}
            <div className="neon-box">

                {/* LAYER 1: BACKGROUND IMAGE (Behind everything) */}
                <img src={chartGraph} alt="Background Chart" className="chart-bg" />

                {/* LAYER 2: CONTENT (On top of image) */}
                <div className="text-content">
                    <h1>BEGIN YOUR JOURNEY TO</h1>
                    <h1>SMART TRADES TODAY!</h1>
                </div>

                <button className="start-btn" onClick={() => navigate('/login')}>
                    Start now!
                </button>

            </div>

        </div>
    );
};

export default Landing;