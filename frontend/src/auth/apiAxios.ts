import axios, { AxiosInstance, AxiosError, InternalAxiosRequestConfig, AxiosResponse } from 'axios';

type AuthContextType = {
	accessToken: string | null;
	refresh: () => Promise<string>;
	logout: () => void;
}

const apiAxios: AxiosInstance = axios.create();

export const setupInterceptors = (auth: AuthContextType) => {
	// Request interceptor to add Authorization header
	apiAxios.interceptors.request.use(
		(config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
			if (auth.accessToken) {
				config.headers.Authorization = `Bearer ${auth.accessToken}`;
			}
			return config;
		},
		(error: AxiosError): Promise<AxiosError> => Promise.reject(error)
	);

	// Response interceptor to handle 401 errors and refresh token
	apiAxios.interceptors.response.use(
		(response: AxiosResponse): AxiosResponse => response,
		async (error: AxiosError): Promise<any> => {
			if (error.response?.status === 401) {
				try {
					const newToken = await auth.refresh();
					if (error.config) {
						error.config.headers.Authorization = `Bearer ${newToken}`;
						return apiAxios(error.config); // Retry original request with new token
					}
				} catch (refreshErr) {
					auth.logout();
					return Promise.reject(refreshErr);
				}
			}
			return Promise.reject(error);
		}
	);
};

export default apiAxios;
