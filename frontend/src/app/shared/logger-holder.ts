// public logging holder for static contexts or for use in function without context
// and unable to inject the NxLogger
export class LoggerHolder {
  public static logger: any = console;
}
