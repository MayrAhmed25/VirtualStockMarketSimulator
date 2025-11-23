import React, { useEffect, useState } from 'react';
import api from '../services/api';

const MarketFeed = () => {
    const [trades, setTrades] = useState([]);

    const fetchMarket = async () => {
        try {
            const res = await api.get('/market/activity');
            setTrades(res.data.slice(0, 25)); // Show more trades (25)
        } catch (error) { console.error(error); }
    };

    // Helper to style the bots
    const getBotStyle = (username) => {
        if (username.includes('Whale')) return { class: 'bot-whale', icon: '🐋' };
        if (username.includes('Hft')) return { class: 'bot-hft', icon: '⚡' };
        if (username.includes('Aggressive')) return { class: 'bot-aggressive', icon: '🐂' };
        if (username.includes('Timid')) return { class: 'bot-timid', icon: '🛡️' };
        if (username.includes('Bot')) return { class: 'bot-random', icon: '🤖' };
        return { class: '', icon: '👤' }; // Real User
    };

    useEffect(() => {
        fetchMarket();
        const interval = setInterval(fetchMarket, 1000); // Fast refresh (1s) for HFT speed
        return () => clearInterval(interval);
    }, []);

    return (
        <div className="card" style={{ height: '450px', overflowY: 'hidden', display:'flex', flexDirection:'column' }}>
            <h3 style={{ borderBottom: '1px solid var(--border-color)', paddingBottom: '10px', marginTop: 0, fontSize: '1rem', color: 'var(--accent-gold)', display:'flex', justifyContent:'space-between' }}>
                <span>🌐 Global Market Feed</span>
                <span style={{fontSize:'0.8em', color:'var(--text-muted)'}}>Live</span>
            </h3>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginTop:'10px', overflowY:'auto', paddingRight:'5px' }}>
                {trades.map(t => {
                    const style = getBotStyle(t.user.username);
                    return (
                        <div key={t.id} className="feed-item" style={{ alignItems:'center' }}>

                            {/* Time */}
                            <span style={{ color: 'var(--text-muted)', fontSize:'0.75em', width:'50px' }}>
                                {new Date(t.timestamp).toLocaleTimeString([], {hour12:false, hour:'2-digit', minute:'2-digit', second:'2-digit'})}
                            </span>

                            {/* User/Bot Name */}
                            <span className={style.class} style={{ flex:1, whiteSpace:'nowrap', overflow:'hidden', textOverflow:'ellipsis', fontSize:'0.85em' }}>
                                {style.icon} {t.user.username.replace('Bot_', '')}
                            </span>

                            {/* Action */}
                            <span style={{
                                color: t.type === 'BUY' ? 'var(--accent-green)' : 'var(--accent-red)',
                                fontWeight:'bold', fontSize:'0.8em', padding:'0 5px'
                            }}>
                                {t.type}
                            </span>

                            {/* Details */}
                            <span style={{fontSize:'0.85em', textAlign:'right', width:'80px'}}>
                                {t.quantity} <strong>{t.stock.ticker}</strong>
                            </span>
                        </div>
                    );
                })}
            </div>
        </div>
    );
};

export default MarketFeed;