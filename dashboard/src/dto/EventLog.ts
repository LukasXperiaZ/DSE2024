export class EventLog {
  _id: Id;
  type: Type;
  timestamp: EventDate;
  lv: string;
  fv: string;

  // Optional attributes. Only if type=FM_check_fail
  message?: string;
  successive_check_fails?: number;
  speed_mismatch?: number;
  lane_mismatch?: string;
}

export class Id {
  $oid: string
}

export class EventDate {
  $date: Date;
}

export enum Type {
  FM_end = "followme_end",
  FM_start = "followme_start",
  FM_check_fail = "followme_check_fail"
}
