import {Injectable} from '@angular/core';
import { ApplicationInsights, ITelemetryItem } from '@microsoft/applicationinsights-web';
import { connectionString } from 'environments/environment';

@Injectable()
export class AppInsightsService {
	appInsights: ApplicationInsights;
	constructor() {
		this.appInsights = new ApplicationInsights({
			config: {
		
				connectionString: connectionString.connectionString,				
				enableAutoRouteTracking: true, // option to log all route changes
			}
		});

		this.appInsights.loadAppInsights();
        this.appInsights.trackPageView();
	}
	


	logPageView(name?: string, url?: string) { // option to call manually
		this.appInsights.trackPageView({
			name: name,
			uri: url
		});
	}

	logEvent(name: string, properties?: { [key: string]: any }) {
		this.appInsights.trackEvent({ name: name }, properties);
	}

	logMetric(name: string, average: number, properties?: { [key: string]: any }) {
		this.appInsights.trackMetric({ name: name, average: average }, properties);
	}

	logException(exception: Error, severityLevel?: number) {
		this.appInsights.trackException({ exception: exception, severityLevel: severityLevel });
	}

	logTrace(message: string, properties?: { [key: string]: any }) {
		this.appInsights.trackTrace({ message: message }, properties);
	}
	
	flush() {
		this.appInsights.flush();
	}
	
	addTelemetryInitializer(telemetryInitializer: (item: ITelemetryItem) => boolean | void) {
		this.appInsights.addTelemetryInitializer(telemetryInitializer);
	}
}