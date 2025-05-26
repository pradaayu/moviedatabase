import React from 'react';

type Props = {
	visible: boolean;
	countdown: number;
	onStayLoggedIn: () => void;
	onLogout: () => void;
};

const SessionTimeoutModal: React.FC<Props> = ({ visible, countdown, onStayLoggedIn, onLogout }) => {
	if (!visible) return null;

	return (
		<div style={styles.container}>
			<div style={styles.card}>
				<h4 style={styles.title}>Session Expiring</h4>
				<p style={styles.message}>
					Your session will expire in <strong>{countdown}</strong> seconds.
				</p>
				<div style={styles.actions}>
					<button onClick={onStayLoggedIn} style={styles.stayButton}>
						Stay Logged In
					</button>
					<button onClick={onLogout} style={styles.logoutButton}>
						Log Out
					</button>
				</div>
			</div>
		</div>
	);
};

const styles: { [key: string]: React.CSSProperties } = {
	container: {
		position: 'fixed',
		bottom: '20px',
		right: '20px',
		zIndex: 9999,
	},
	card: {
		backgroundColor: '#fff',
		borderRadius: '8px',
		boxShadow: '0 4px 8px rgba(0, 0, 0, 0.15)',
		padding: '16px 20px',
		minWidth: '260px',
		maxWidth: '300px',
		fontFamily: 'sans-serif',
	},
	title: {
		margin: '0 0 8px',
		fontSize: '16px',
		color: '#333',
	},
	message: {
		fontSize: '14px',
		marginBottom: '12px',
		color: '#555',
	},
	actions: {
		display: 'flex',
		justifyContent: 'flex-end',
		gap: '8px',
	},
	stayButton: {
		backgroundColor: '#4CAF50',
		color: '#fff',
		border: 'none',
		borderRadius: '4px',
		padding: '6px 12px',
		cursor: 'pointer',
	},
	logoutButton: {
		backgroundColor: '#f44336',
		color: '#fff',
		border: 'none',
		borderRadius: '4px',
		padding: '6px 12px',
		cursor: 'pointer',
	},
};

export default SessionTimeoutModal;
