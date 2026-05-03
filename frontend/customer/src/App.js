import React, { useState, useEffect } from 'react';
import './index.css'; 

const FALLBACK_MENU_DATA = [
  { id: 1, name: 'Americano', desc: 'Espresso with hot water, smooth finish', prices: { Regular: 1.50, Large: 2.00 }, img: 'https://images.unsplash.com/photo-1559525839-b184a4d698c7?auto=format&fit=crop&w=150&q=80' },
  { id: 2, name: 'Americano with Milk', desc: 'Americano with added milk', prices: { Regular: 2.00, Large: 2.50 }, img: 'https://images.unsplash.com/photo-1578314675249-a6910f80cc4e?auto=format&fit=crop&w=150&q=80' },
  { id: 3, name: 'Latte', desc: 'Smooth espresso with steamed milk', prices: { Regular: 2.50, Large: 3.00 }, img: 'https://images.unsplash.com/photo-1570968915860-54d5c301fa9f?auto=format&fit=crop&w=150&q=80' },
  { id: 4, name: 'Cappuccino', desc: 'Espresso with steamed milk and foam', prices: { Regular: 2.50, Large: 3.00 }, img: 'https://images.unsplash.com/photo-1534778101976-62847782c213?auto=format&fit=crop&w=150&q=80' },
  { id: 5, name: 'Hot Chocolate', desc: 'Rich and creamy chocolate drink', prices: { Regular: 2.00, Large: 2.50 }, img: 'https://images.unsplash.com/photo-1542990253-0d0f5be5f0ed?auto=format&fit=crop&w=150&q=80' },
  { id: 6, name: 'Mocha', desc: 'Espresso with chocolate and steamed milk', prices: { Regular: 2.50, Large: 3.00 }, img: 'https://images.unsplash.com/photo-1572442388796-11668a67e53d?auto=format&fit=crop&w=150&q=80' },
  { id: 7, name: 'Mineral Water', desc: 'Refreshing bottled water', prices: { Regular: 1.00 }, img: 'https://images.unsplash.com/photo-1523362628745-0c100150b504?auto=format&fit=crop&w=150&q=80' },
];

export default function App() {
  const [currentPage, setCurrentPage] = useState('login');
  const [cart, setCart] = useState([]);
  const [isCartOpen, setIsCartOpen] = useState(false);
  const [orderInfo, setOrderInfo] = useState({ name: '', phone: '', date: '', time: '', orderNumber: '' });
  
  const [menuData, setMenuData] = useState(FALLBACK_MENU_DATA);

  useEffect(() => {
    fetch('/api/menu')
      .then(response => {
        if (!response.ok) throw new Error("Backend not available");
        return response.json();
      })
      .then(data => {
        console.log("Successfully connected to backend! Real menu data:", data);
      })
      .catch(err => {
        console.log("Backend or database is not connected. Falling back to test data. Error:", err.message);
      });
  }, []);

  const cartTotal = cart.reduce((sum, item) => sum + (item.price * item.qty), 0);
  const cartCount = cart.reduce((sum, item) => sum + item.qty, 0);

  const handleAddToCart = (product, size, qty, price) => {
    setCart(prev => {
      const existing = prev.find(item => item.id === product.id && item.size === size);
      if (existing) return prev.map(item => item === existing ? { ...item, qty: item.qty + qty } : item);
      return [...prev, { ...product, size, qty, price }];
    });
  };

  const updateCartItem = (index, delta) => {
    const newCart = [...cart];
    newCart[index].qty += delta;
    if (newCart[index].qty <= 0) newCart.splice(index, 1);
    setCart(newCart);
  };

  return (
    <div className="mobile-container">
      {currentPage === 'login' && <LoginScreen onNext={() => setCurrentPage('menu')} />}
      
      {currentPage === 'menu' && (
        <MenuScreen 
          menuData={menuData} 
          onAdd={handleAddToCart} 
          cartCount={cartCount}
          onOpenCart={() => setIsCartOpen(true)}
          onBack={() => setCurrentPage('login')}
        />
      )}

      {currentPage === 'pickup' && (
        <PickupScreen 
          orderInfo={orderInfo} 
          setOrderInfo={setOrderInfo}
          onBack={() => setCurrentPage('menu')}
          onNext={() => setCurrentPage('payment')} 
        />
      )}

      {currentPage === 'payment' && (
        <PaymentScreen 
          total={cartTotal}
          onBack={() => setCurrentPage('pickup')}
          onConfirm={() => {
            const orderPayload = {
              customerId: 1, 
              pickupTime: `${orderInfo.date}T${orderInfo.time}:00`, 
              items: cart.map(item => ({
                menuItemId: item.id,
                quantity: item.qty
              }))
            };

            console.log("Order payload preparing to send:", orderPayload);

            fetch('/api/orders', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(orderPayload)
            })
            .then(response => {
              if (response.ok) {
                return response.json();
              } else {
                throw new Error("Backend no response");
              }
            })
            .then(data => {
              setOrderInfo(prev => ({ ...prev, orderNumber: data.id || `#CF${Math.floor(100 + Math.random() * 900)}` }));
              setCurrentPage('confirmation');
            })
            .catch(err => {
              alert("Notice: Backend is not connected, but the UI will simulate a successful payment!");
              setOrderInfo(prev => ({ ...prev, orderNumber: `#CF${Math.floor(100 + Math.random() * 900)}` }));
              setCurrentPage('confirmation');
            });
          }} 
        />
      )}

      {currentPage === 'confirmation' && (
        <ConfirmationScreen 
          orderInfo={orderInfo} 
          cart={cart} 
          total={cartTotal}
          onOrderAgain={() => {
            setCart([]); 
            setOrderInfo({ name: '', phone: '', date: '', time: '', orderNumber: '' });
            setCurrentPage('menu');
          }} 
        />
      )}

      {isCartOpen && (
        <CartSheet 
          cart={cart} total={cartTotal} 
          onClose={() => setIsCartOpen(false)} 
          onUpdate={updateCartItem}
          onCheckout={() => { setIsCartOpen(false); setCurrentPage('pickup'); }}
        />
      )}
    </div>
  );
}

/* =========================================
   Page Components
   ========================================= */

function LoginScreen({ onNext }) {
  return (
    <div style={{ padding: '40px 20px', textAlign: 'center', height: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
      <div style={{ width: '80px', height: '80px', backgroundColor: 'var(--primary-blue)', borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 20px auto', boxShadow: '0 4px 10px rgba(0, 98, 204, 0.2)' }}>
        <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
          <path d="M18 8h1a4 4 0 0 1 0 8h-1"></path>
          <path d="M2 8h16v9a4 4 0 0 1-4 4H6a4 4 0 0 1-4-4V8z"></path>
          <line x1="6" y1="1" x2="6" y2="4"></line>
          <line x1="10" y1="1" x2="10" y2="4"></line>
          <line x1="14" y1="1" x2="14" y2="4"></line>
        </svg>
      </div>
      <h2 style={{ color: '#002B5B' }}>Whistlestop Coffee Hut</h2>
      <p style={{ margin: '10px 0 30px', color: '#666' }}>Welcome! Please login to order</p>
      
      <div className="form-group" style={{ padding: 0 }}>
        <input type="email" placeholder="Email Address" />
        <input type="password" placeholder="Password" style={{ marginTop: '15px' }} />
      </div>
      
      <button className="btn-primary" style={{ marginTop: '20px' }} onClick={onNext}>Sign In</button>
      <p style={{ margin: '20px 0', color: '#666', fontSize: '14px' }}>Or continue with</p>
      <button className="btn-primary" style={{ background: '#fff', color: '#333', border: '1px solid #ddd', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '10px' }} onClick={onNext}>
        <strong style={{ fontSize: '18px' }}>G</strong> Continue with Google
      </button>
    </div>
  );
}

function MenuScreen({ menuData, onAdd, cartCount, onOpenCart, onBack }) {
  return (
    <div>
      <div className="header">
        <span className="back-btn" onClick={onBack}>← Cramlington Station</span>
        <div style={{ position: 'relative', cursor: 'pointer', fontSize: '20px' }} onClick={onOpenCart}>
          🛒 {cartCount > 0 && <span style={{ background: '#dc3545', color: 'white', borderRadius: '50%', padding: '2px 6px', fontSize: '12px', position: 'absolute', top: -8, right: -10 }}>{cartCount}</span>}
        </div>
      </div>
      <div style={{ padding: '0 20px 20px' }}>
        <h2>Good Morning ☕</h2>
        <p style={{ color: '#666' }}>What would you like today?</p>
      </div>
      <div className="menu-list">
        {menuData.map(item => <ProductCard key={item.id} product={item} onAdd={onAdd} />)}
      </div>
    </div>
  );
}

function ProductCard({ product, onAdd }) {
  const [size, setSize] = useState('Regular');
  const [qty, setQty] = useState(1);
  const currentPrice = product.prices[size] || product.prices['Regular']; 

  return (
    <div className="product-card">
      <div className="product-header">
        <img src={product.img} alt={product.name} className="product-img" />
        <div>
          <h3 style={{ fontSize: '16px' }}>{product.name}</h3>
          <p style={{ fontSize: '12px', color: '#666', marginTop: '4px' }}>{product.desc}</p>
        </div>
      </div>
      <div className="size-selector">
        {Object.keys(product.prices).map(s => (
          <button key={s} className={`size-btn ${size === s ? 'active' : ''}`} onClick={() => setSize(s)}>
            <div>{s}</div>
            <strong>£{product.prices[s].toFixed(2)}</strong>
          </button>
        ))}
      </div>
      <div className="action-row">
        <div className="qty-control">
          <button onClick={() => setQty(Math.max(1, qty - 1))}>-</button>
          <span>{qty}</span>
          <button onClick={() => setQty(qty + 1)}>+</button>
        </div>
        <button className="btn-primary" style={{ flex: 1 }} onClick={() => { onAdd(product, size, qty, currentPrice); setQty(1); }}>
          Add £{(currentPrice * qty).toFixed(2)}
        </button>
      </div>
    </div>
  );
}

function CartSheet({ cart, total, onClose, onUpdate, onCheckout }) {
  return (
    <div className="cart-overlay" onClick={onClose}>
      <div className="cart-sheet" onClick={e => e.stopPropagation()}>
        <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '20px' }}>
          <h3>Shopping Cart</h3>
          <span onClick={onClose} style={{ cursor: 'pointer', fontSize: '18px' }}>✕</span>
        </div>
        <div style={{ overflowY: 'auto', flex: 1, marginBottom: '20px' }}>
          {cart.length === 0 ? (
             <div style={{ textAlign: 'center', padding: '40px 0', color: '#999' }}>🛍️<br/>Your cart is empty</div>
          ) : (
            cart.map((item, index) => (
              <div key={index} className="cart-item">
                <div>
                  <div style={{ fontWeight: 'bold' }}>{item.name}</div>
                  <div style={{ fontSize: '12px', color: '#666' }}>{item.size} Size</div>
                  <div className="qty-control" style={{ marginTop: '10px', display: 'inline-flex' }}>
                    <button onClick={() => onUpdate(index, -1)}>-</button>
                    <span>{item.qty}</span>
                    <button onClick={() => onUpdate(index, 1)}>+</button>
                  </div>
                </div>
                <div style={{ textAlign: 'right' }}>
                  <div className="trash-icon" onClick={() => onUpdate(index, -item.qty)}>🗑</div>
                  <div style={{ marginTop: '15px', color: 'var(--primary-blue)', fontWeight: 'bold' }}>£{(item.price * item.qty).toFixed(2)}</div>
                </div>
              </div>
            ))
          )}
        </div>
        {cart.length > 0 && (
          <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontWeight: 'bold', marginBottom: '15px', fontSize: '18px' }}>
              <span>Total</span>
              <span style={{ color: 'var(--primary-blue)' }}>£{total.toFixed(2)}</span>
            </div>
            <button className="btn-primary" onClick={onCheckout}>Checkout</button>
          </div>
        )}
      </div>
    </div>
  );
}

function PickupScreen({ orderInfo, setOrderInfo, onBack, onNext }) {
  const [dateType, setDateType] = useState('text'); 
  const isFormValid = orderInfo.name && orderInfo.phone && orderInfo.date && orderInfo.time;

  return (
    <div>
      <div className="header"><span className="back-btn" onClick={onBack}>← Pickup Details</span></div>
      
      <div style={{ padding: '0 20px' }}>
        <div className="product-card" style={{ display: 'flex', gap: '15px', alignItems: 'center' }}>
           <span style={{ color: 'var(--primary-blue)', fontSize: '24px' }}>📍</span>
           <div>
             <div style={{ fontWeight: 'bold', fontSize: '16px' }}>Cramlington Station</div>
             <div style={{ color: '#666', fontSize: '14px' }}>Whistlestop Coffee Hut</div>
           </div>
        </div>

        <div className="notice-card">
          <span className="notice-icon">!</span>
          <span className="notice-text" style={{ color: '#d32f2f' }}>Important Notice<br/>Orders not picked up within 15 minutes of the scheduled time will be automatically cancelled. You'll need to place a new order.</span>
        </div>
      </div>

      <div className="form-group">
        <label>Full Name</label>
        <input type="text" value={orderInfo.name} onChange={e => setOrderInfo({...orderInfo, name: e.target.value})} />
      </div>
      <div className="form-group">
        <label>Phone Number</label>
        <input type="tel" value={orderInfo.phone} onChange={e => setOrderInfo({...orderInfo, phone: e.target.value})} />
      </div>
      <div className="form-group">
        <label>Pickup Date</label>
        <input 
          type={dateType} 
          placeholder="DD/MM/YY" 
          value={orderInfo.date} 
          onFocus={() => setDateType('date')}
          onBlur={(e) => { if(!e.target.value) setDateType('text') }}
          onChange={e => setOrderInfo({...orderInfo, date: e.target.value})} 
        />
      </div>
      <div className="form-group">
        <label>Pickup Time</label>
        <input type="time" value={orderInfo.time} onChange={e => setOrderInfo({...orderInfo, time: e.target.value})} />
        <div style={{ fontSize: '12px', color: '#666', marginTop: '8px' }}>Please allow at least 10 minutes for order preparation !</div>
      </div>
      <div style={{ padding: '20px' }}>
        <button className="btn-primary" disabled={!isFormValid} onClick={onNext}>Continue to Payment</button>
      </div>
    </div>
  );
}

function PaymentScreen({ total, onBack, onConfirm }) {
  const [method, setMethod] = useState('card');
  return (
    <div>
       <div className="header"><span className="back-btn" onClick={onBack}>← Payment</span></div>
       <div style={{ padding: '0 20px' }}>
         <div className="product-card" style={{ fontSize: '18px', fontWeight: 'bold' }}>
           Total: £{total.toFixed(2)}
         </div>
         
         <div style={{ marginTop: '20px' }}>
           <div className={`payment-method ${method === 'card' ? 'active' : ''}`} onClick={() => setMethod('card')}>
              <div className="radio-circle"></div> 💳 Credit/Debit Card
           </div>
           
           {method === 'card' && (
             <div className="product-card" style={{ background: '#f8f9fa', marginTop: '-5px', marginBottom: '15px', borderTopLeftRadius: 0, borderTopRightRadius: 0 }}>
                <input type="text" placeholder="Card Number (1234 5678 9012 3456)" style={{ width: '100%', padding: '10px', marginBottom: '10px', border: '1px solid #ddd', borderRadius: '4px' }}/>
                <input type="text" placeholder="Cardholder Name (John Doe)" style={{ width: '100%', padding: '10px', marginBottom: '10px', border: '1px solid #ddd', borderRadius: '4px' }}/>
                <div style={{ display: 'flex', gap: '10px' }}>
                  <input type="text" placeholder="MM/YY" style={{ flex: 1, padding: '10px', border: '1px solid #ddd', borderRadius: '4px' }}/>
                  <input type="text" placeholder="CVV" style={{ flex: 1, padding: '10px', border: '1px solid #ddd', borderRadius: '4px' }}/>
                </div>
             </div>
           )}

           <div className={`payment-method ${method === 'wallet' ? 'active' : ''}`} onClick={() => setMethod('wallet')}>
              <div className="radio-circle"></div> 📱 Digital Wallet
           </div>
           
           {method === 'wallet' && (
             <div style={{ padding: '20px', textAlign: 'center', color: '#666', background: '#fff', borderRadius: '8px' }}>
               You'll be redirected to complete payment via your digital wallet.
             </div>
           )}
         </div>
         
         <button className="btn-primary" style={{ marginTop: '20px' }} onClick={onConfirm}>Confirm & Pay £{total.toFixed(2)}</button>
       </div>
    </div>
  );
}

function ConfirmationScreen({ orderInfo, cart, total, onOrderAgain }) {
  return (
    <div style={{ padding: '20px', background: 'var(--bg-color)', minHeight: '100vh' }}>
      <div className="success-icon-container">
        <div className="success-icon">✓</div>
      </div>
      <h2 style={{ textAlign: 'center' }}>Order Confirmed!</h2>
      <p style={{ textAlign: 'center', color: '#666', marginBottom: '20px' }}>Your coffee will be ready for pickup</p>
      
      <div className="product-card" style={{ display: 'flex', justifyContent: 'space-between', fontSize: '16px' }}>
        <span>Order Number:</span>
        <strong style={{ color: 'var(--primary-blue)' }}>{orderInfo.orderNumber}</strong>
      </div>

      <div className="product-card" style={{ background: '#e6f4ea', display: 'flex', justifyContent: 'space-between' }}>
        <span>Pickup Time:</span>
        <strong>{orderInfo.date} {orderInfo.time}</strong>
      </div>

      <div className="notice-card" style={{ alignItems: 'center' }}>
          <span className="notice-icon">⏱</span>
          <span className="notice-text">Please arrive on time. Orders not picked up within 15 minutes will be automatically cancelled.</span>
      </div>

      <h3 style={{ fontSize: '16px', margin: '20px 0 10px' }}>Order Items</h3>
      <div className="product-card">
        {cart.map((item, idx) => (
          <div key={idx} className="order-detail-row" style={{ borderBottom: idx !== cart.length - 1 ? '1px solid #eee' : 'none', paddingBottom: idx !== cart.length - 1 ? '10px' : '0' }}>
            <div>
              <strong>{item.name}</strong>
              <div style={{ fontSize: '12px', color: '#666' }}>{item.size} • Qty: {item.qty}</div>
            </div>
            <div style={{ color: 'var(--primary-blue)' }}>£{(item.price * item.qty).toFixed(2)}</div>
          </div>
        ))}
        <div style={{ borderTop: '2px dashed #ddd', marginTop: '10px', paddingTop: '10px', display: 'flex', justifyContent: 'space-between', fontWeight: 'bold', fontSize: '16px' }}>
          <span>Total Paid</span>
          <span style={{ color: 'var(--primary-blue)' }}>£{total.toFixed(2)}</span>
        </div>
      </div>

      <h3 style={{ fontSize: '16px', margin: '20px 0 10px' }}>Pickup Location</h3>
      <div className="product-card">
        <div style={{ display: 'flex', gap: '10px', marginBottom: '10px' }}>
          <span>📍</span>
          <div>
            <strong>Whistlestop Coffee Hut</strong>
            <div style={{ color: '#666', fontSize: '14px' }}>Cramlington Station</div>
          </div>
        </div>
        <div style={{ marginTop: '10px', color: '#333', fontSize: '14px' }}>
          <div>{orderInfo.name}</div>
          <div>{orderInfo.phone}</div>
        </div>
      </div>

      <button className="btn-primary" style={{ marginTop: '30px', marginBottom: '40px' }} onClick={onOrderAgain}>Order Again</button>
    </div>
  );
}