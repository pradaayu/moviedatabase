import React, { ReactNode } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuthContext } from './AuthProvider';

type PrivateRouteProps = {
	children: ReactNode;
}

const PrivateRoute: React.FC<PrivateRouteProps> = ({ children }) => {
	const { isAuthenticated, isLoading } = useAuthContext();
	
	if (isLoading) {
		return (
			<div className="flex justify-center items-center h-screen text-white">
				Checking authentication...
			</div>
		)
	}
	
	if (!isAuthenticated) {
		return <Navigate to="/login" replace />
	}
	
	// Render nested routes or direct children:
	// The <Outlet> component is a placeholder within a parent route's component 
	// ... that tells React Router where to render the child routes.
	// See: https://dev.to/mana95/understanding-about-react-outlet-with-proper-routing-2mo
	return children ? <>{children}</> : <Outlet />;
};

export default PrivateRoute;
