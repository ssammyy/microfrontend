import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
//import { ApproveRejectConsignmentComponent } from './approve-reject-consignment/approve-reject-consignment.component';


@Component({
  selector: 'app-motor-vehicle-inspection-single-view',
  templateUrl: './motor-vehicle-inspection-single-view.component.html',
  styleUrls: ['./motor-vehicle-inspection-single-view.component.css']
})
export class MotorVehicleInspectionSingleViewComponent implements OnInit {
  active = 'consignee';
  constructor(  private dialog: MatDialog) { }

  ngOnInit(): void {
  }
  approveRejectConsignment() {
    // let ref = this.dialog.open(ApproveRejectConsignmentComponent, {
    //     data: {        }
    // });
    // ref.afterClosed()
    //     .subscribe(
    //         res => {
    //             console.log(res)
               
    //         }
    //     )
}
}
