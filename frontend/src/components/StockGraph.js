import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

const StockGraph = ({ stockId, stockTicker }) => {
    const [data, setData] = useState([]);
    const [color, setColor] = useState('#3b82f6');

    useEffect(() => {
        const fetchHistory = async () => {
            try {
                const res = await api.get(`/stocks/${stockId}/history`);

                const formattedData = res.data.map(item => ({
                    time: new Date(item.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
                    price: parseFloat(item.price)
                }));

                if (formattedData.length > 1) {
                    const startPrice = formattedData[0].price;
                    const endPrice = formattedData[formattedData.length - 1].price;
                    setColor(endPrice >= startPrice ? '#22c55e' : '#ef4444');
                }

                setData(formattedData);
            } catch (err) {
                console.error("Failed to load graph", err);
            }
        };

        fetchHistory();
        const interval = setInterval(fetchHistory, 10000); // <--- CHANGED TO 10 SECONDS
        return () => clearInterval(interval);
    }, [stockId]);

    return (
        <div className="card chart-container" style={{border: `1px solid ${color}`, boxShadow: `0 0 10px ${color}33`}}>
            <div style={{ marginBottom: '15px', borderBottom:'1px solid rgba(255,255,255,0.1)', paddingBottom:'10px', display:'flex', justifyContent:'space-between' }}>
                <h2 style={{ color: color, fontSize:'1.5rem', margin:0 }}>
                    {stockTicker}
                </h2>
                {data.length > 0 && (
                    <h2 style={{ color: 'white', fontSize:'1.5rem', margin:0 }}>
                        ${data[data.length-1].price.toFixed(2)}
                    </h2>
                )}
            </div>

            <ResponsiveContainer width="100%" height={320}>
                <AreaChart data={data}>
                    <defs>
                        <linearGradient id="colorPrice" x1="0" y1="0" x2="0" y2="1">
                            <stop offset="5%" stopColor={color} stopOpacity={0.4}/>
                            <stop offset="95%" stopColor={color} stopOpacity={0}/>
                        </linearGradient>
                    </defs>
                    <CartesianGrid strokeDasharray="3 3" stroke="#334155" vertical={false} />
                    <XAxis dataKey="time" stroke="#94a3b8" style={{fontSize:'0.8rem'}} tick={{dy: 10}} />
                    <YAxis
                        domain={['auto', 'auto']}
                        stroke="#94a3b8"
                        style={{fontSize:'0.8rem'}}
                        tick={{dx: -10}}
                        tickFormatter={(number) => `$${number.toFixed(2)}`}
                    />
                    <Tooltip
                        contentStyle={{backgroundColor:'#1e293b', borderColor: color, color:'#fff', borderRadius:'8px'}}
                        itemStyle={{color: color}}
                        formatter={(value) => [`$${value.toFixed(2)}`, "Price"]}
                    />
                    <Area
                        type="monotone"
                        dataKey="price"
                        stroke={color}
                        strokeWidth={3}
                        fillOpacity={1}
                        fill="url(#colorPrice)"
                        animationDuration={500}
                    />
                </AreaChart>
            </ResponsiveContainer>
        </div>
    );
};

export default StockGraph;