import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';
import MarketFeed from './MarketFeed';
import GameSummary from './GameSummary';
import StockGraph from './StockGraph';
import logo from '../assets/logo.svg';

const Dashboard = () => {
    // --- STATE ---
    const [stocks, setStocks] = useState([]);
    const [wallet, setWallet] = useState(null);
    const [portfolio, setPortfolio] = useState([]);
    const [history, setHistory] = useState([]);
    const [quantities, setQuantities] = useState({});

    const [selectedStock, setSelectedStock] = useState(null);
    const [gameTime, setGameTime] = useState(1800);
    const [isMarketOpen, setIsMarketOpen] = useState(true);
    const [showSummary, setShowSummary] = useState(false);

    const navigate = useNavigate();

    // --- 1. DATA FETCHING ---
    const fetchData = async () => {
        try {
            const stockRes = await api.get('/stocks');
            setStocks(stockRes.data);

            const walletRes = await api.get('/wallet');
            setWallet(walletRes.data);
            const portfolioRes = await api.get('/portfolio');
            setPortfolio(portfolioRes.data);
            const historyRes = await api.get('/portfolio/history');
            setHistory(historyRes.data.slice().reverse());
        } catch (error) { console.error("Error", error); }
    };

    // --- 2. SLOW POLL (10 Seconds) ---
    useEffect(() => {
        fetchData(); // Run immediately
        const interval = setInterval(fetchData, 10000); // <--- CHANGED TO 10 SECONDS
        return () => clearInterval(interval);
    }, []);

    // --- 3. GAME CLOCK ---
    useEffect(() => {
        if (!isMarketOpen) return;
        const timer = setInterval(() => {
            setGameTime(prev => {
                if (prev <= 1) {
                    clearInterval(timer);
                    setIsMarketOpen(false);
                    setShowSummary(true);
                    return 0;
                }
                return prev - 1;
            });
        }, 1000);
        return () => clearInterval(timer);
    }, [isMarketOpen]);

    // --- 4. AUTO-SELECT LOGIC ---
    useEffect(() => {
        if (stocks.length > 0 && selectedStock === null) {
            setSelectedStock(stocks[0]);
        }
    }, [stocks, selectedStock]);


    // --- HELPERS & TRADING ---
    const formatTime = (seconds) => {
        const m = Math.floor(seconds / 60);
        const s = seconds % 60;
        return `${m}:${s < 10 ? '0' : ''}${s}`;
    };

    const getOwnedQuantity = (stockId) => {
        const holding = portfolio.find(p => p.stock.id === stockId);
        return holding ? holding.quantity : 0;
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    const handleTrade = async (e, stockId, type) => {
        e.stopPropagation();
        if (!isMarketOpen) return alert("🚫 Market Closed!");
        const qty = quantities[stockId] || 0;
        if (qty <= 0) return alert("Enter valid quantity!");

        try {
            await api.post(`/trade/${type}`, { stockId, quantity: parseInt(qty) });
            fetchData(); // Immediate refresh after trade
            setQuantities({ ...quantities, [stockId]: '' });
        } catch (error) { alert("Trade Failed!"); }
    };

    return (
        <div className="dashboard-container">

            {showSummary && <GameSummary onClose={() => {setShowSummary(false); setGameTime(1800); setIsMarketOpen(true);}} />}

            <div className="header-bar">
                <div style={{display:'flex', alignItems:'center', gap:'30px'}}>
                    <img src={logo} alt="Nexus Logo" style={{ height: '50px', filter: 'drop-shadow(0 0 10px var(--electric-pink))' }} />
                    <div className={`timer-badge ${gameTime < 60 ? 'urgent' : ''}`}>
                        ⏱️ {formatTime(gameTime)}
                    </div>
                </div>

                <div style={{ display: 'flex', gap: '15px', alignItems: 'center' }}>
                    <button onClick={() => setShowSummary(true)} className="btn-primary">
                        🏁 End Day
                    </button>
                    <div className="wallet-badge">
                        ${wallet ? wallet.cashBalance.toFixed(2) : '...'}
                    </div>
                    <button onClick={handleLogout} className="btn-logout">
                        Logout
                    </button>
                </div>
            </div>

            <div style={{ marginBottom: '30px' }}>
                {selectedStock && (
                    <StockGraph
                        stockId={selectedStock.id}
                        stockTicker={selectedStock.ticker}
                    />
                )}
            </div>

            <div className="sidebar-layout">

                <div>
                    <h2 style={{marginBottom:'20px'}}>📉 Market</h2>
                    <div className="stock-grid">
                        {stocks.map(s => {
                            const owned = getOwnedQuantity(s.id);
                            const isSelected = selectedStock?.id === s.id;
                            return (
                                <div
                                    key={s.id}
                                    className="stock-card"
                                    onClick={() => setSelectedStock(s)}
                                    style={{
                                        border: isSelected ? '1px solid var(--electric-pink)' : '',
                                        boxShadow: isSelected ? 'var(--glow-pink)' : ''
                                    }}
                                >
                                    <div className="stock-header">
                                        <h3>{s.ticker}</h3>
                                        <span className="stock-price">${s.currentPrice.toFixed(2)}</span>
                                    </div>
                                    <div className="own-badge">Own: <strong>{owned}</strong></div>

                                    <div style={{ display: 'flex', gap: '10px' }} onClick={(e) => e.stopPropagation()}>
                                        <input
                                            type="number" placeholder="#"
                                            value={quantities[s.id] || ''}
                                            onChange={(e) => setQuantities({ ...quantities, [s.id]: e.target.value })}
                                            style={{ width: '60px' }}
                                        />
                                        <button onClick={(e) => handleTrade(e, s.id, 'buy')} className="btn-buy">BUY</button>
                                        <button onClick={(e) => handleTrade(e, s.id, 'sell')} disabled={owned===0} className="btn-sell">SELL</button>
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                </div>

                <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                    <MarketFeed />
                    <div className="card" style={{height: '300px', overflowY:'auto'}}>
                        <h3 style={{marginBottom:'15px', borderBottom:'1px solid rgba(255,255,255,0.1)', paddingBottom:'10px'}}>📜 My History</h3>
                        <div style={{display:'flex', flexDirection:'column', gap:'10px'}}>
                            {history.map(t => (
                                <div key={t.id} className="history-item">
                                    <span style={{color: t.type==='BUY'?'var(--accent-green)':'var(--accent-red)', fontWeight:'bold'}}>
                                        {t.type}
                                    </span>
                                    <span>{t.quantity} <strong>{t.stock.ticker}</strong></span>
                                    <span style={{color:'var(--text-muted)'}}>${t.priceAtExecution.toFixed(2)}</span>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>

            </div>
        </div>
    );
};

export default Dashboard;