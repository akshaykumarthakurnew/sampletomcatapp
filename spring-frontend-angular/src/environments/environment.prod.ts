/*
 *
 *  * Copyright 2016-2017 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

import { DynamicEnvironment } from './dynamic-environment';
import { DynamicConnectionString } from './dynamic-connectionstring';
import { DynamicRestUrl } from './dynamic-resturl';
import { DynamicCloudRoleInstance } from './dynamic-roleinstance';
import { DynamicCloudRole } from './dynamic-rolename';

class Environment extends DynamicEnvironment {

	public production: boolean;
	constructor() {
		super();
		this.production = false;
	}
}

class Rolename extends DynamicCloudRole {

	public production: boolean;
	constructor() {
		super();
		this.production = false;
	}
}


class Roleinstance extends DynamicCloudRoleInstance {

	public production: boolean;
	constructor() {
		super();
		this.production = false;
	}
}

class Resturl extends DynamicRestUrl {

	public production: boolean;
	constructor() {
		super();
		this.production = false;
	}
}

class Connectionstring extends DynamicConnectionString {

	public production: boolean;
	constructor() {
		super();
		this.production = false;
	}
}
export const environment = new Environment();
export const connectionString = new Connectionstring();
export const REST_API_URL = new Resturl();
export const roleInstance = new Roleinstance();
export const roleName = new Rolename();
