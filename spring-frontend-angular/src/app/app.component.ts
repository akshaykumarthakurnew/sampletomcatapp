import { Component, OnInit } from '@angular/core';
import { AppInsightsService } from './logging/logging.service';
import { connectionString, REST_API_URL,roleName,roleInstance } from './../environments/environment';


@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

	title = 'Spring-Frontend-Angular';
	insightService: AppInsightsService;

	constructor(appInsightsService: AppInsightsService) {
		this.insightService = appInsightsService;
		console.log("connectionString:"+connectionString.connectionString);
		console.log("REST_API_URL:"+REST_API_URL.REST_API_URL);
		console.log("roleName:"+roleName.cloudRole);
	}

	ngOnInit(): void {

		var telemetryInitializer = (envelope) => {
			envelope.tags["ai.cloud.role"] = roleName.cloudRole;
			envelope.tags["ai.cloud.roleInstance"] = roleInstance.cloudRoleInstance;

		}
		
		this.insightService.addTelemetryInitializer(telemetryInitializer);

		this.insightService.logPageView('owners');
		this.insightService.logEvent('some event');
		this.insightService.logPageView('some page');
		this.insightService.logException(new Error('some error'));
		this.insightService.logTrace('some trace');
		this.insightService.flush();

	}
}
