declare var window: any;

export class DynamicCloudRoleInstance {
	public get cloudRoleInstance() {
		return window.config.cloudRoleInstance;
	}
}