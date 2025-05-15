import { toast, ToastOptions } from 'react-toastify';
// https://deadsimplechat.com/blog/react-toastify-the-complete-guide/

const defaultOptions: ToastOptions = {
	position: 'bottom-right',
	autoClose: 4000,
	hideProgressBar: false,
	closeOnClick: true,
	pauseOnHover: true,
	draggable: true,
};

export const useNotifier = () => {
	return {
		success: (msg: string, opts: ToastOptions = {}) =>
			toast.success(msg, { ...defaultOptions, ...opts }),
		error: (msg: string, opts: ToastOptions = {}) =>
			toast.error(msg, { ...defaultOptions, ...opts }),
		info: (msg: string, opts: ToastOptions = {}) =>
			toast.info(msg, { ...defaultOptions, ...opts }),
		warning: (msg: string, opts: ToastOptions = {}) =>
			toast.warn(msg, { ...defaultOptions, ...opts }),
	};
};
