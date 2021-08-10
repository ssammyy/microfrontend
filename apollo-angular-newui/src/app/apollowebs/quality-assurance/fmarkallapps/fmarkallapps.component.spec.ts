import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FmarkallappsComponent } from './fmarkallapps.component';

describe('FmarkallappsComponent', () => {
  let component: FmarkallappsComponent;
  let fixture: ComponentFixture<FmarkallappsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FmarkallappsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FmarkallappsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
