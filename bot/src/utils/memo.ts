export class Memo<T> {
	public constructor(private getter: () => MemoResult<T>) {
		this.lastResult = getter();
	}

	public get value() {
		if (this.lastResult.validUntil < new Date()) {
			this.lastResult = this.getter();
		}

		return this.lastResult.value;
	}

	private lastResult: MemoResult<T>;
}

export interface MemoResult<T> {
	value: T;
	validUntil: Date;
}
