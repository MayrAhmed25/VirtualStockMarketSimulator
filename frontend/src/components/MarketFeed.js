import React, { useEffect, useState } from 'react';
import api from '../services/api';

const MarketFeed = () => {
    const [trades, setTrades] = useState([]);

    const fetchMarket = async () => {
        try {
            const res = await api.get('/market/activity');
            setTrades(res.data.slice(0, 20));
        } catch (error) { console.error(error); }
    };

    useEffect(() => {
        fetchMarket();
        const interval = setInterval(fetchMarket, 15000); // <--- CHANGED TO 15 SECONDS
        return () => clearInterval(interval);
    }, []);

    return (
        <div className="card" style={{ height: '400px', overflowY: 'hidden' }}>
            <h3 style={{ borderBottom: '1px solid var(--border-color)', paddingBottom: '10px', marginTop: 0, fontSize: '1rem', color: 'var(--accent-gold)' }}>
                🌐 Global Feed
            </h3>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', marginTop:'10px' }}>
                {trades.map(t => (
                    <div key={t.id} className="feed-item">
                        <span style={{ color: 'var(--text-muted)', fontSize:'0.8em' }}>
                            {new Date(t.timestamp).toLocaleTimeString([], {hour:'2-digit', minute:'2-digit', second:'2-digit'})}
                        </span>
                        <span style={{ fontWeight: 'bold', color: t.user.username.startsWith('Bot') ? 'var(--accent-gold)' : 'white' }}>
                            {t.user.username}
                        </span>
                        <span style={{ color: t.type === 'BUY' ? 'var(--accent-green)' : 'var(--accent-red)', fontWeight:'bold' }}>
                            {t.type}
                        </span>
                        <span>
                            {t.quantity} <strong>{t.stock.ticker}</strong>
                        </span>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MarketFeed;