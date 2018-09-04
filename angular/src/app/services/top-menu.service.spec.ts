import {inject, TestBed} from '@angular/core/testing';

import {TopMenuService} from './top-menu.service';

describe('TopMenuService', () => {
	beforeEach(() => {
		TestBed.configureTestingModule({
			providers: [TopMenuService]
		});
	});

	it('should be created', inject([TopMenuService], (service: TopMenuService) => {
		expect(service).toBeTruthy();
	}));
});
