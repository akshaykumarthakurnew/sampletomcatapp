declare var window: any;

export class DynamicRestUrl {
	public get REST_API_URL() {
		return window.config.REST_API_URL;
	}
}