import React, { useEffect, useState } from 'react';
import api from '../services/api';

const GameSummary = ({ onClose }) => {
    const [stats, setStats] = useState(null);

    useEffect(() => {
        api.get('/analysis/summary').then(res => setStats(res.data));
    }, []);

    if (!stats) return null;

    return (
        <div style={{
            position: 'fixed', top: 0, left: 0, width: '100%', height: '100%',
            backgroundColor: 'rgba(5, 0, 10, 0.9)', // Deep dark overlay
            display: 'flex', justifyContent: 'center', alignItems: 'center',
            zIndex: 1000, backdropFilter: 'blur(8px)'
        }}>
            <div className="card" style={{
                maxWidth: '500px', width: '90%', textAlign: 'center',
                // --- THE ELECTRIC PINK GLOW ---
                border: '3px solid var(--electric-pink)',
                boxShadow: '0 0 50px var(--glow-pink)',
                backgroundColor: 'rgba(15, 7, 25, 0.95)' // Slightly darker glass for contrast
            }}>
                <h1 style={{ fontSize: '2.5em', margin: '0 0 10px 0', color: 'var(--electric-pink)', textShadow: '0 0 10px var(--electric-pink)' }}>
                    🏁 Market Closed
                </h1>

                <h2 style={{ color: 'var(--accent-gold)', fontSize: '2em', margin: '10px 0', textTransform: 'uppercase', textShadow: '0 0 10px rgba(234, 179, 8, 0.5)' }}>
                    {stats.performanceGrade}
                </h2>

                <div style={{ margin: '30px 0', textAlign: 'left', fontSize: '1.2em', lineHeight: '2em', backgroundColor: 'rgba(255,255,255,0.03)', padding: '20px', borderRadius: '10px', border: '1px solid rgba(255,255,255,0.05)' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                        <span style={{color:'var(--text-muted)'}}>💰 Final Cash:</span>
                        <span>${stats.finalCash.toFixed(2)}</span>
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                        <span style={{color:'var(--text-muted)'}}>📈 Assets Value:</span>
                        <span>${stats.portfolioValue.toFixed(2)}</span>
                    </div>
                    <hr style={{ borderColor: 'rgba(255,255,255,0.1)', margin: '15px 0' }} />
                    <div style={{ display: 'flex', justifyContent: 'space-between', fontWeight: 'bold', color: 'var(--accent-green)', fontSize: '1.3em' }}>
                        <span>💎 Net Worth:</span>
                        <span>${stats.totalNetWorth.toFixed(2)}</span>
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', color: stats.profitLoss >= 0 ? 'var(--accent-green)' : 'var(--accent-red)', marginTop: '10px' }}>
                        <span>Profit / Loss:</span>
                        <span>{stats.profitLoss >= 0 ? '+' : ''}${stats.profitLoss.toFixed(2)}</span>
                    </div>
                </div>

                <button
                    onClick={onClose}
                    className="btn-primary"
                    style={{
                        width: '100%', fontSize: '1.2em', fontWeight: 'bold',
                        background: 'var(--electric-pink)',
                        boxShadow: '0 0 20px rgba(214, 0, 255, 0.4)',
                        color: 'white'
                    }}
                >
                    Close Report
                </button>
            </div>
        </div>
    );
};

export default GameSummary;