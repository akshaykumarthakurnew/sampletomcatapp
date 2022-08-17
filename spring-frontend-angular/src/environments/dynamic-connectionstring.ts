declare var window: any;

export class DynamicConnectionString {
	public get connectionString() {
		return window.config.connectionString;
	}
}