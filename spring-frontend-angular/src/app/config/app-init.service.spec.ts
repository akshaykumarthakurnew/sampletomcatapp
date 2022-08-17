/* tslint:disable:no-unused-variable */

import { TestBed, inject, waitForAsync } from '@angular/core/testing';
import { AppInitService } from './app-init.service';

describe('Service: AppInit', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AppInitService]
    });
  });

  it('should ...', inject([AppInitService], (service: AppInitService) => {
    expect(service).toBeTruthy();
  }));
});
