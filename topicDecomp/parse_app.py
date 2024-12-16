import os
import argparse
import json
import uuid

def parse_results(app, output_path: str) -> tuple[list[dict[str, int]], list[float]]:
    with open(os.path.join(output_path, f"{app}_clustering.txt"), 'r') as f:
        lines = [l.replace("\n", "") for l in f.readlines()]
        clusterings_sep = '----------------------------------------------------------------------------------------------------'
    clusterings = list()
    clustering = list()
    for line in lines:
        if line == clusterings_sep:
            if len(clustering) == 0:
                continue
            clusterings.append(clustering)
            clustering = list()
        else:
            clustering.append(line)
    parsed_clusterings = list()
    resolutions = list()
    modularities = list()
    for clustering in clusterings:
        clusters = list()
        cluster = list()
        for line in clustering:
            if line.startswith("Modularity") or line == "":
                modularities.append(line.replace("Modularity ", ""))
                continue
            if line.startswith("Resolution"):
                resolutions.append(line.replace("Resolution ", ""))
                continue
            if line.startswith("Service"):
                if len(cluster) != 0:
                    clusters.append(cluster)
                cluster = list()
                continue
            cluster.append(line)
        if len(cluster) != 0:
            clusters.append(cluster)
        parsed_clusterings.append(clusters)
    for i, clusters in enumerate(parsed_clusterings):
        sizes = [len(cluster) for cluster in clusters]
    return parsed_clusterings, modularities
    # partitions_encodings = list()
    # for clusters in parsed_clusterings:
    #     partitions_encoding = dict()
    #     for i, cluster in enumerate(clusters):
    #         for c in cluster:
    #             partitions_encoding[c] = i
    #     partitions_encodings.append(partitions_encoding)
    # return partitions_encodings, resolutions


def to_microRefact(partitions_encodings, modularities):
    decompositions = dict()
    decomp_id = uuid.uuid4()
    decompositions["id"] = str(decomp_id)
    decompositions["name"] = app
    decompositions["rootPath"] = ""
    decompositions["relativePath"] = app
    all_partitions = list()
    for j, p in enumerate(partitions_encodings):
        partition = dict()
        skip_partition = False
        for i, cluster in enumerate(p):
            partition[int(i)] = cluster
            if len(cluster) < 2:
                skip_partition = True
                break
        if skip_partition:
            continue
        all_partitions.append(partition)
        partition_to_use = partition
        if modularities[j] == "":
            for m in range(j, -1, -1):
                if modularities[m] != "":
                    modularity_to_use = modularities[m]
        else:
            modularity_to_use = modularities[j]
        break
    # json_string = json.dumps(all_partitions)
    json_string = str(partition_to_use)
    decompositions["clusterString"] = json_string
    decompositions["commitHash"] = ""
    decompositions["Modularity"] = float(modularity_to_use)
    return decompositions





if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Parse results from topic decomposition')
    parser.add_argument('app', type=str, help='Application name')
    args = parser.parse_args()
    app = args.app
    output_path = os.path.join(os.curdir, "outputs", app, "raw_results")
    partitions_encodings, modularities = parse_results(app, output_path)
    microRefact_decomp = to_microRefact(partitions_encodings, modularities)
    save_path = os.path.join(os.curdir, "outputs", app, "parsed_microRefact")
    name = f"project_{app}.json"
    with open(os.path.join(save_path, name), "w") as f:
        json.dump([microRefact_decomp], f, indent=4)
