declare var window: any;

export class DynamicCloudRole {
	public get cloudRole() {
		return window.config.cloudRole;
	}
}