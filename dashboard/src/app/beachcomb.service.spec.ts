import { TestBed } from '@angular/core/testing';

import { BeachCombService } from './beach-comb.service';

describe('BeachcombService', () => {
  let service: BeachCombService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BeachCombService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
