import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-import-inspection',
  templateUrl: './import-inspection.component.html',
  styleUrls: ['./import-inspection.component.css']
})
export class ImportInspectionComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
  }
  goTo(url: string, isExemption: boolean): void {
    if(isExemption) {

    } else {
      this.router.navigateByUrl(url)
    }
  }
}
