export type TUser = {
	name: string,
	password: string,
	confirmPassword: string,
	email: string,
} 

export type ApiResponse = {
	success: boolean,
	message: string,
	data: any,
	errorCode: string,
	timestamp: string,
}